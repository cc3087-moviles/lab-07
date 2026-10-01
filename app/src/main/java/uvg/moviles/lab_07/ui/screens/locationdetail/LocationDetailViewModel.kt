package uvg.moviles.lab_07.ui.screens.locationdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.LocationDb
import uvg.moviles.lab_07.navigation.LocationDetailDestination

class LocationDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val locationDb = LocationDb()
    private val route: LocationDetailDestination = savedStateHandle.toRoute()

    var location by mutableStateOf<Location?>(null)
        private set

    init {
        location = locationDb.getLocationById(route.locationId)
    }
}
