package uvg.moviles.lab_07.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.data.AppDataRepository
import uvg.moviles.lab_07.data.UserPreferencesRepository

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val appDataRepository: AppDataRepository
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun login(onLoginCompleted: () -> Unit) {
        if (_name.value.trim().isEmpty() || _isSyncing.value) return

        viewModelScope.launch {
            _isSyncing.value = true
            try {
                appDataRepository.syncIfNeeded()
                userPreferencesRepository.saveName(_name.value)
                onLoginCompleted()
            } finally {
                _isSyncing.value = false
            }
        }
    }
}
