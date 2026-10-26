package uvg.moviles.lab_07.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.CharacterDb
import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.LocationDb
import uvg.moviles.lab_07.data.local.CharacterDao
import uvg.moviles.lab_07.data.local.LocationDao
import uvg.moviles.lab_07.data.mapper.toEntity
import uvg.moviles.lab_07.data.mapper.toModel

class AppDataRepository(
    private val characterDao: CharacterDao,
    private val locationDao: LocationDao
) {
    val characters: Flow<List<Character>> = characterDao.observeAll()
        .map { entities -> entities.map { it.toModel() } }

    fun character(id: Int): Flow<Character?> = characterDao.observeById(id)
        .map { it?.toModel() }

    suspend fun setFavorite(id: Int, value: Boolean) {
        characterDao.updateFavorite(id, value)
    }

    val locations: Flow<List<Location>> = locationDao.observeAll()
        .map { entities -> entities.map { it.toModel() } }

    fun location(id: Int): Flow<Location?> = locationDao.observeById(id)
        .map { it?.toModel() }

    suspend fun syncIfNeeded() {
        if (characterDao.count() > 0 && locationDao.count() > 0) return

        delay(4_000)
        characterDao.insertAll(CharacterDb().getAllCharacters().map { it.toEntity() })
        locationDao.insertAll(LocationDb().getAllLocations().map { it.toEntity() })
    }

    suspend fun clearAll() {
        characterDao.deleteAll()
        locationDao.deleteAll()
    }
}
