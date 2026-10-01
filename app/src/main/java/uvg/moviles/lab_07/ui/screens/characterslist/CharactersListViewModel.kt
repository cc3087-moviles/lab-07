package uvg.moviles.lab_07.ui.screens.characterslist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.CharacterDb

class CharactersListViewModel : ViewModel() {
    private val characterDb = CharacterDb()

    var characters by mutableStateOf<List<Character>>(emptyList())
        private set

    init {
        characters = characterDb.getAllCharacters()
    }
}
