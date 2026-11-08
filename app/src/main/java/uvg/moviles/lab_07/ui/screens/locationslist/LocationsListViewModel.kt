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
import uvg.moviles.lab_07.data.LocationRepository

data class LocationsListUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

enum class LocationFilter { ALL, FAVORITES, NOT_FAVORITES }

class LocationsListViewModel(private val locationRepository: LocationRepository) : ViewModel() {
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(LocationsListUiState())
    val uiState: StateFlow<LocationsListUiState> = _uiState.asStateFlow()

    private val _filter = MutableStateFlow(LocationFilter.ALL)
    val filter: StateFlow<LocationFilter> = _filter.asStateFlow()

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

    fun setFilter(filter: LocationFilter) { _filter.value = filter }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch { locationRepository.setFavorite(location.id, !location.isFavorite) }
    }

    private fun loadData() {
        _uiState.value = LocationsListUiState()
        loadJob = viewModelScope.launch {
            delay(4000)
            locationRepository.observeAll().collect { locations ->
                _uiState.value = LocationsListUiState(isLoading = false, data = locations)
            }
        }
    }
}
