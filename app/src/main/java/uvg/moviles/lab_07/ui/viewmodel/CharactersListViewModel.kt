package uvg.moviles.lab_07.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.CharacterDb

/**
 * ViewModel encargado de exponer el listado completo de personajes
 * a la pantalla de Characters.
 */
class CharactersListViewModel : ViewModel() {

    private val characterDb = CharacterDb()

    var characters by mutableStateOf<List<Character>>(emptyList())
        private set

    init {
        characters = characterDb.getAllCharacters()
    }
}
