package uvg.moviles.lab_07

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import uvg.moviles.lab_07.data.UserPreferencesRepository
import uvg.moviles.lab_07.ui.screens.login.LoginViewModel
import uvg.moviles.lab_07.ui.screens.profile.ProfileViewModel
import uvg.moviles.lab_07.ui.screens.startup.StartupViewModel

class RickAndMortyApplication : Application() {
    val userPreferencesRepository by lazy { UserPreferencesRepository(applicationContext) }
}

val appViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        StartupViewModel(application.userPreferencesRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        LoginViewModel(application.userPreferencesRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        ProfileViewModel(application.userPreferencesRepository)
    }
}
