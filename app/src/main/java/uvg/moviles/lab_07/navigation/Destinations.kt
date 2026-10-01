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
object MainTabs

@Serializable
object CharactersGraph

@Serializable
object CharactersListDestination

@Serializable
data class CharacterDetailDestination(val characterId: Int)

@Serializable
object LocationsGraph

@Serializable
object LocationsListDestination

@Serializable
data class LocationDetailDestination(val locationId: Int)

@Serializable
object ProfileDestination
