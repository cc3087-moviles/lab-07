package uvg.moviles.lab_07.ui.screens.locationdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.data.AppDataRepository
import uvg.moviles.lab_07.navigation.LocationDetailDestination

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val appDataRepository: AppDataRepository
) : ViewModel() {
    private val route: LocationDetailDestination = savedStateHandle.toRoute()
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(LocationDetailUiState())
    val uiState: StateFlow<LocationDetailUiState> = _uiState.asStateFlow()

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
        _uiState.value = LocationDetailUiState()
        loadJob = viewModelScope.launch {
            delay(2000)
            appDataRepository.location(route.locationId).filterNotNull().collect { location ->
                _uiState.value = LocationDetailUiState(isLoading = false, data = location)
            }
        }
    }
}
