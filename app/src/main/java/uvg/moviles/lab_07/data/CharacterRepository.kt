package uvg.moviles.lab_07.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.data.local.CharacterDao
import uvg.moviles.lab_07.data.local.CharacterEntity
import uvg.moviles.lab_07.data.mapper.toModel

interface CharacterRepository {
    fun observeAll(): Flow<List<Character>>
    fun observeById(id: Int): Flow<Character?>
    suspend fun create(name: String, status: String, species: String, gender: String, image: String): Int
    suspend fun update(id: Int, name: String, status: String, species: String, gender: String, image: String)
    suspend fun delete(id: Int)
    suspend fun setFavorite(id: Int, value: Boolean)
}

class RoomCharacterRepository(private val characterDao: CharacterDao) : CharacterRepository {
    override fun observeAll(): Flow<List<Character>> = characterDao.observeAll().map { list -> list.map { it.toModel() } }
    override fun observeById(id: Int): Flow<Character?> = characterDao.observeById(id).map { it?.toModel() }

    override suspend fun create(name: String, status: String, species: String, gender: String, image: String): Int {
        return characterDao.create(CharacterEntity(0, name, status, species, gender, image)).toInt()
    }

    override suspend fun update(id: Int, name: String, status: String, species: String, gender: String, image: String) {
        characterDao.updateFields(id, name, status, species, gender, image)
    }
    override suspend fun delete(id: Int) = characterDao.delete(id)
    override suspend fun setFavorite(id: Int, value: Boolean) = characterDao.updateFavorite(id, value)
}
