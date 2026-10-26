package uvg.moviles.lab_07

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.room.Room
import uvg.moviles.lab_07.data.AppDataRepository
import uvg.moviles.lab_07.data.UserPreferencesRepository
import uvg.moviles.lab_07.data.local.AppDatabase
import uvg.moviles.lab_07.ui.screens.characterdetail.CharacterDetailViewModel
import uvg.moviles.lab_07.ui.screens.characterslist.CharactersListViewModel
import uvg.moviles.lab_07.ui.screens.login.LoginViewModel
import uvg.moviles.lab_07.ui.screens.locationdetail.LocationDetailViewModel
import uvg.moviles.lab_07.ui.screens.locationslist.LocationsListViewModel
import uvg.moviles.lab_07.ui.screens.profile.ProfileViewModel
import uvg.moviles.lab_07.ui.screens.startup.StartupViewModel

class RickAndMortyApplication : Application() {
    val userPreferencesRepository by lazy { UserPreferencesRepository(applicationContext) }
    val database: AppDatabase by lazy {
        Room.databaseBuilder<AppDatabase>(applicationContext, AppDatabase::class.java, "rick_and_morty.db")
            .build()
    }
    val appDataRepository by lazy {
        AppDataRepository(database.characterDao(), database.locationDao())
    }
}

val appViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        StartupViewModel(application.userPreferencesRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        LoginViewModel(application.userPreferencesRepository, application.appDataRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        ProfileViewModel(application.userPreferencesRepository, application.appDataRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        CharactersListViewModel(application.appDataRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        CharacterDetailViewModel(createSavedStateHandle(), application.appDataRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        LocationsListViewModel(application.appDataRepository)
    }
    initializer {
        val application = checkNotNull(this[APPLICATION_KEY]) as RickAndMortyApplication
        LocationDetailViewModel(createSavedStateHandle(), application.appDataRepository)
    }
}
