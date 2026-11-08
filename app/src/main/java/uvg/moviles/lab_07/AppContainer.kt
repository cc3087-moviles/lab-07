package uvg.moviles.lab_07

import android.content.Context
import androidx.room.Room
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import uvg.moviles.lab_07.data.ApiSyncRepository
import uvg.moviles.lab_07.data.CharacterRepository
import uvg.moviles.lab_07.data.LocationRepository
import uvg.moviles.lab_07.data.RoomCharacterRepository
import uvg.moviles.lab_07.data.RoomLocationRepository
import uvg.moviles.lab_07.data.SyncRepository
import uvg.moviles.lab_07.data.local.AppDatabase
import uvg.moviles.lab_07.data.remote.RickAndMortyApi

class AppContainer(context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    val database: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "rick_and_morty.db")
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build()
    val api: RickAndMortyApi = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RickAndMortyApi::class.java)
    val characterRepository: CharacterRepository = RoomCharacterRepository(database.characterDao())
    val locationRepository: LocationRepository = RoomLocationRepository(database.locationDao())
    val syncRepository: SyncRepository = ApiSyncRepository(context, database, api)
}
