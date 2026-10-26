package uvg.moviles.lab_07.ui.screens.characterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.data.AppDataRepository
import uvg.moviles.lab_07.navigation.CharacterDetailDestination

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val appDataRepository: AppDataRepository
) : ViewModel() {
    private val route: CharacterDetailDestination = savedStateHandle.toRoute()
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

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

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            appDataRepository.setFavorite(character.id, !character.isFavorite)
        }
    }

    private fun loadData() {
        _uiState.value = CharacterDetailUiState()
        loadJob = viewModelScope.launch {
            delay(2000)
            appDataRepository.character(route.characterId).filterNotNull().collect { character ->
                _uiState.value = CharacterDetailUiState(isLoading = false, data = character)
            }
        }
    }
}
