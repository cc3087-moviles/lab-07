package uvg.moviles.lab_07.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.CharacterDb
import uvg.moviles.lab_07.navigation.CharacterDetail

/**
 * ViewModel encargado del detalle de un personaje.
 *
 * Recibe el argumento de navegación (el ID del personaje) a través del
 * SavedStateHandle que Navigation-Compose inyecta automáticamente cuando
 * se crea el ViewModel dentro de un composable<CharacterDetail> { ... }.
 * Con esto, la pantalla de Characters solo tuvo que mandar el ID; este
 * ViewModel es el que resuelve el resto de la información del personaje
 * consultando el "CharacterDb".
 */
class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterDb = CharacterDb()

    private val route: CharacterDetail = savedStateHandle.toRoute()

    var character by mutableStateOf<Character?>(null)
        private set

    init {
        character = characterDb.getCharacterById(route.characterId)
    }
}
