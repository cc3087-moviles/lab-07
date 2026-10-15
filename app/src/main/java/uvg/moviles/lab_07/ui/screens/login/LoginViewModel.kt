package uvg.moviles.lab_07.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.data.UserPreferencesRepository

class LoginViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun login(onLoginCompleted: () -> Unit) {
        if (_name.value.trim().isEmpty()) return

        viewModelScope.launch {
            userPreferencesRepository.saveName(_name.value)
            onLoginCompleted()
        }
    }
}
