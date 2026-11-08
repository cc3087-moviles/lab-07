package uvg.moviles.lab_07.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Insert
    suspend fun insert(entity: CharacterEntity): Long

    @Query("SELECT COALESCE(MAX(id), 0) + 1 FROM characters")
    suspend fun nextId(): Int

    @Transaction
    suspend fun create(entity: CharacterEntity): Long = insert(entity.copy(id = nextId()))

    @Query("UPDATE characters SET name = :name, status = :status, species = :species, gender = :gender, image = :image WHERE id = :id")
    suspend fun updateFields(id: Int, name: String, status: String, species: String, gender: String, image: String)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM characters ORDER BY id DESC")
    fun observeAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun observeById(id: Int): Flow<CharacterEntity?>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun count(): Int

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}
