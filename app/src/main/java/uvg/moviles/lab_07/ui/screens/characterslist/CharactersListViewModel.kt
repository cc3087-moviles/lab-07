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
import uvg.moviles.lab_07.data.AppDataRepository

data class CharactersListUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersListViewModel(private val appDataRepository: AppDataRepository) : ViewModel() {
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(CharactersListUiState())
    val uiState: StateFlow<CharactersListUiState> = _uiState.asStateFlow()

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites.asStateFlow()

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

    fun toggleFavoritesFilter() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            appDataRepository.setFavorite(character.id, !character.isFavorite)
        }
    }

    private fun loadData() {
        _uiState.value = CharactersListUiState()
        loadJob = viewModelScope.launch {
            delay(4000)
            appDataRepository.characters.collect { characters ->
                _uiState.value = CharactersListUiState(isLoading = false, data = characters)
            }
        }
    }
}
