package uvg.moviles.lab_07.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.data.AppDataRepository
import uvg.moviles.lab_07.data.UserPreferencesRepository

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val appDataRepository: AppDataRepository
) : ViewModel() {
    val name: StateFlow<String> = userPreferencesRepository.name
        .map { it.orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    fun logout(onLogoutCompleted: () -> Unit) {
        viewModelScope.launch {
            userPreferencesRepository.clearName()
            appDataRepository.clearAll()
            onLogoutCompleted()
        }
    }
}
