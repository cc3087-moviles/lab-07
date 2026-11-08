package uvg.moviles.lab_07.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import uvg.moviles.lab_07.data.SyncRepository
import uvg.moviles.lab_07.data.UserPreferencesRepository

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()

    fun onNameChanged(name: String) {
        _name.value = name
        _hasError.value = false
    }

    fun login(onLoginCompleted: () -> Unit) {
        if (_name.value.trim().isEmpty() || _isSyncing.value) return

        viewModelScope.launch {
            _isSyncing.value = true
            _hasError.value = false
            try {
                syncRepository.syncIfNeeded()
                userPreferencesRepository.saveName(_name.value)
                onLoginCompleted()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e("LoginViewModel", "Falló la sincronización inicial", exception)
                _hasError.value = true
            } finally {
                _isSyncing.value = false
            }
        }
    }
}
