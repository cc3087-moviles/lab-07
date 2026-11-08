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
import uvg.moviles.lab_07.data.CharacterRepository

data class CharactersListUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

enum class CharacterFilter { ALL, FAVORITES, NOT_FAVORITES }

class CharactersListViewModel(private val characterRepository: CharacterRepository) : ViewModel() {
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(CharactersListUiState())
    val uiState: StateFlow<CharactersListUiState> = _uiState.asStateFlow()

    private val _filter = MutableStateFlow(CharacterFilter.ALL)
    val filter: StateFlow<CharacterFilter> = _filter.asStateFlow()

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

    fun setFilter(filter: CharacterFilter) { _filter.value = filter }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            characterRepository.setFavorite(character.id, !character.isFavorite)
        }
    }

    private fun loadData() {
        _uiState.value = CharactersListUiState()
        loadJob = viewModelScope.launch {
            delay(4000)
            characterRepository.observeAll().collect { characters ->
                _uiState.value = CharactersListUiState(isLoading = false, data = characters)
            }
        }
    }
}
