package uvg.moviles.lab_07.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.withTransaction
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.sync.withLock
import uvg.moviles.lab_07.data.local.AppDatabase
import uvg.moviles.lab_07.data.mapper.toEntity
import uvg.moviles.lab_07.data.remote.RickAndMortyApi
import retrofit2.HttpException
import java.io.IOException

interface SyncRepository {
    suspend fun syncIfNeeded()
    suspend fun clearAll()
}

class ApiSyncRepository(
    context: Context,
    private val database: AppDatabase,
    private val api: RickAndMortyApi
) : SyncRepository {
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    private val requestMutex = Mutex()
    private var lastRequestStartedAt = 0L
    override suspend fun syncIfNeeded() {
        val characterDao = database.characterDao()
        val locationDao = database.locationDao()
        if (characterDao.count() > 0 && locationDao.count() > 0) return
        check(hasValidatedInternet()) { "No hay conexión a internet disponible" }

        coroutineScope {
            val charactersFirst = async { requestWithRetry { api.getCharacters(1) } }
            val locationsFirst = async { requestWithRetry { api.getLocations(1) } }
            val charactersPage = charactersFirst.await()
            val locationsPage = locationsFirst.await()
            // El API público limita ráfagas; dos solicitudes en paralelo evita los 429
            // sin dejar de descargar las páginas concurrentemente.
            val semaphore = Semaphore(2)
            val remainingCharacters = async { fetchRemaining(charactersPage.info.pages, semaphore, api::getCharacters) }
            val remainingLocations = async { fetchRemaining(locationsPage.info.pages, semaphore, api::getLocations) }
            val characters = charactersPage.results + remainingCharacters.await()
            val locations = locationsPage.results + remainingLocations.await()

            database.withTransaction {
                characterDao.deleteAll()
                locationDao.deleteAll()
                characterDao.insertAll(characters.map { it.toEntity() })
                locationDao.insertAll(locations.map { it.toEntity() })
            }
        }
    }

    override suspend fun clearAll() = database.withTransaction {
        database.characterDao().deleteAll()
        database.locationDao().deleteAll()
    }

    private suspend fun <T> fetchRemaining(pages: Int, semaphore: Semaphore, request: suspend (Int) -> uvg.moviles.lab_07.data.remote.PagedResponse<T>): List<T> {
        if (pages <= 1) return emptyList()
        return coroutineScope {
            (2..pages).map { page -> async { semaphore.withPermit { requestWithRetry { request(page) }.results } } }
                .awaitAll().flatten()
        }
    }

    private suspend fun <T> requestWithRetry(request: suspend () -> T): T {
        var lastError: Exception? = null
        repeat(MAX_RETRIES) { attempt ->
            try {
                throttleRequests()
                return request()
            } catch (exception: HttpException) {
                if (exception.code() != 429 && exception.code() !in 500..599) throw exception
                lastError = exception
            } catch (exception: IOException) {
                lastError = exception
            }
            delay(RETRY_DELAY_MS * (1L shl attempt))
        }
        throw checkNotNull(lastError)
    }

    private suspend fun throttleRequests() {
        requestMutex.withLock {
            val elapsed = System.currentTimeMillis() - lastRequestStartedAt
            if (elapsed < REQUEST_INTERVAL_MS) delay(REQUEST_INTERVAL_MS - elapsed)
            lastRequestStartedAt = System.currentTimeMillis()
        }
    }

    private fun hasValidatedInternet(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private companion object {
        const val MAX_RETRIES = 6
        const val RETRY_DELAY_MS = 1_000L
        const val REQUEST_INTERVAL_MS = 350L
    }
}
