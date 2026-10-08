package uvg.moviles.lab_07.ui.screens.characterslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.CharacterDb

data class CharactersListUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersListViewModel : ViewModel() {
    private val characterDb = CharacterDb()
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(CharactersListUiState())
    val uiState: StateFlow<CharactersListUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun onLoadingClicked() {
        loadJob?.cancel()
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }

    fun retry() {
        loadJob?.cancel()
        loadData()
    }

    private fun loadData() {
        _uiState.value = CharactersListUiState()
        loadJob = viewModelScope.launch {
            delay(4000)
            _uiState.value = CharactersListUiState(
                isLoading = false,
                data = characterDb.getAllCharacters()
            )
        }
    }
}
