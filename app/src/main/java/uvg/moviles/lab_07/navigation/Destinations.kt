package uvg.moviles.lab_07.navigation

import kotlinx.serialization.Serializable

/**
 * Destinos de la app, usados con Navigation-Compose (destinations tipados).
 * No se usan rutas de tipo String: cada pantalla se identifica con una clase
 * anotada con @Serializable.
 */

@Serializable
object Login

@Serializable
object CharactersList

/**
 * A "Character details" únicamente se le manda el ID del personaje,
 * nada más (ni el nombre, ni la imagen, ni el resto de la información).
 */
@Serializable
data class CharacterDetail(val characterId: Int)
