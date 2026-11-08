package uvg.moviles.lab_07

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.createSavedStateHandle
import uvg.moviles.lab_07.data.UserPreferencesRepository
import uvg.moviles.lab_07.ui.screens.characterdetail.CharacterDetailViewModel
import uvg.moviles.lab_07.ui.screens.characterform.CharacterFormViewModel
import uvg.moviles.lab_07.ui.screens.characterslist.CharactersListViewModel
import uvg.moviles.lab_07.ui.screens.login.LoginViewModel
import uvg.moviles.lab_07.ui.screens.locationdetail.LocationDetailViewModel
import uvg.moviles.lab_07.ui.screens.locationslist.LocationsListViewModel
import uvg.moviles.lab_07.ui.screens.profile.ProfileViewModel
import uvg.moviles.lab_07.ui.screens.startup.StartupViewModel

class RickAndMortyApplication : Application() {
    val userPreferencesRepository by lazy { UserPreferencesRepository(applicationContext) }
    val container by lazy { AppContainer(applicationContext) }
}

val appViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        StartupViewModel(application.userPreferencesRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        LoginViewModel(application.userPreferencesRepository, application.container.syncRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        ProfileViewModel(application.userPreferencesRepository, application.container.syncRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        CharactersListViewModel(application.container.characterRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        CharacterDetailViewModel(createSavedStateHandle(), application.container.characterRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        CharacterFormViewModel(createSavedStateHandle(), application.container.characterRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        LocationsListViewModel(application.container.locationRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        LocationDetailViewModel(createSavedStateHandle(), application.container.locationRepository)
    }
}
