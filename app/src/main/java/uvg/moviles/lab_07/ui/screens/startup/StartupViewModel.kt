package uvg.moviles.lab_07.ui.screens.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.data.UserPreferencesRepository

sealed interface StartupState {
    data object Loading : StartupState
    data class SessionResolved(val hasActiveSession: Boolean) : StartupState
}

class StartupViewModel(userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _state = MutableStateFlow<StartupState>(StartupState.Loading)
    val state: StateFlow<StartupState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val name = userPreferencesRepository.name.first()
            _state.value = StartupState.SessionResolved(!name.isNullOrBlank())
        }
    }
}
