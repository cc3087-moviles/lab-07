package uvg.moviles.lab_07.ui.screens.locationslist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.LocationDb

class LocationsListViewModel : ViewModel() {
    private val locationDb = LocationDb()

    var locations by mutableStateOf<List<Location>>(emptyList())
        private set

    init {
        locations = locationDb.getAllLocations()
    }
}
