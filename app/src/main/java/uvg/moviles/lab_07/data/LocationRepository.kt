package uvg.moviles.lab_07.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.data.local.LocationDao
import uvg.moviles.lab_07.data.mapper.toModel

interface LocationRepository {
    fun observeAll(): Flow<List<Location>>
    fun observeById(id: Int): Flow<Location?>
    suspend fun setFavorite(id: Int, value: Boolean)
}

class RoomLocationRepository(private val locationDao: LocationDao) : LocationRepository {
    override fun observeAll(): Flow<List<Location>> = locationDao.observeAll().map { list -> list.map { it.toModel() } }
    override fun observeById(id: Int): Flow<Location?> = locationDao.observeById(id).map { it?.toModel() }
    override suspend fun setFavorite(id: Int, value: Boolean) = locationDao.updateFavorite(id, value)
}
