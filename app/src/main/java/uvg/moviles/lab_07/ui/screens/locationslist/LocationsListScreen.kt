package uvg.moviles.lab_07.ui.screens.locationslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.appViewModelFactory
import uvg.moviles.lab_07.ui.components.ErrorLayout
import uvg.moviles.lab_07.ui.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsListScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationsListViewModel = viewModel(factory = appViewModelFactory)
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Locations") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { innerPadding ->
        when {
            state.isLoading -> LoadingLayout(
                onClick = viewModel::onLoadingClicked,
                modifier = Modifier.padding(innerPadding)
            )
            state.hasError -> ErrorLayout(
                message = "Error al obtener listado de ubicaciones. Intenta de nuevo",
                onRetry = viewModel::retry,
                modifier = Modifier.padding(innerPadding)
            )
            else -> LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                items(state.data, key = { it.id }) { location ->
                    LocationRow(location, onClick = { onLocationClick(location.id) })
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun LocationRow(location: Location, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(location.name, style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface)
        Text(location.type, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
