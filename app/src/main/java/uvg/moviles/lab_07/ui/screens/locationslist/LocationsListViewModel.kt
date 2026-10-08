package uvg.moviles.lab_07.ui.screens.locationslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.LocationDb

data class LocationsListUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsListViewModel : ViewModel() {
    private val locationDb = LocationDb()
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(LocationsListUiState())
    val uiState: StateFlow<LocationsListUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun onLoadingClicked() {
        loadJob?.cancel()
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }

    fun retry() {
        loadJob?.cancel()
        loadData()
    }

    private fun loadData() {
        _uiState.value = LocationsListUiState()
        loadJob = viewModelScope.launch {
            delay(4000)
            _uiState.value = LocationsListUiState(
                isLoading = false,
                data = locationDb.getAllLocations()
            )
        }
    }
}
