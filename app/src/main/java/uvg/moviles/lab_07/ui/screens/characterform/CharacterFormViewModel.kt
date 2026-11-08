package uvg.moviles.lab_07.ui.screens.characterform

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.data.CharacterRepository
import uvg.moviles.lab_07.navigation.CharacterFormDestination

data class CharacterFormUiState(val isLoading: Boolean = true, val name: String = "", val species: String = "", val status: String = "Alive", val gender: String = "Male", val image: String = "")

class CharacterFormViewModel(savedStateHandle: SavedStateHandle, private val repository: CharacterRepository) : ViewModel() {
    private val route: CharacterFormDestination = savedStateHandle.toRoute()
    private val _uiState = MutableStateFlow(CharacterFormUiState())
    val uiState: StateFlow<CharacterFormUiState> = _uiState.asStateFlow()
    val isEditing = route.characterId != null

    init { viewModelScope.launch {
        val character = route.characterId?.let { repository.observeById(it).first() }
        _uiState.value = character?.let { CharacterFormUiState(false, it.name, it.species, it.status, it.gender, it.image) } ?: CharacterFormUiState(isLoading = false)
    } }
    fun update(transform: (CharacterFormUiState) -> CharacterFormUiState) { _uiState.value = transform(_uiState.value) }
    fun save(onSaved: (Int) -> Unit) = viewModelScope.launch {
        val state = _uiState.value
        val id = route.characterId ?: repository.create(state.name.trim(), state.status, state.species.trim(), state.gender, state.image.trim())
        if (route.characterId != null) repository.update(id, state.name.trim(), state.status, state.species.trim(), state.gender, state.image.trim())
        onSaved(id)
    }
}
