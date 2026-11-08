package uvg.moviles.lab_07.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): PagedResponse<CharacterDto>

    @GET("location")
    suspend fun getLocations(@Query("page") page: Int): PagedResponse<LocationDto>
}

@Serializable
data class PagedResponse<T>(val info: PageInfo, val results: List<T>)

@Serializable
data class PageInfo(val count: Int, val pages: Int, val next: String?, val prev: String?)

@Serializable
data class CharacterDto(
    val id: Int, val name: String, val status: String, val species: String,
    val gender: String, val image: String
)

@Serializable
data class LocationDto(val id: Int, val name: String, val type: String, val dimension: String)
