package uvg.moviles.lab_07.ui.screens.characterdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.CharacterDb
import uvg.moviles.lab_07.navigation.CharacterDetailDestination

class CharacterDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val characterDb = CharacterDb()
    private val route: CharacterDetailDestination = savedStateHandle.toRoute()

    var character by mutableStateOf<Character?>(null)
        private set

    init {
        character = characterDb.getCharacterById(route.characterId)
    }
}
