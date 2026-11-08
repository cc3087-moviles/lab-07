package uvg.moviles.lab_07.ui.screens.locationslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import uvg.moviles.lab_07.ui.components.EmptyLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsListScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationsListViewModel = viewModel(factory = appViewModelFactory)
) {
    val state by viewModel.uiState.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val locations = when (filter) {
        LocationFilter.ALL -> state.data
        LocationFilter.FAVORITES -> state.data.filter { it.isFavorite }
        LocationFilter.NOT_FAVORITES -> state.data.filterNot { it.isFavorite }
    }

    Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0), topBar = {
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
            else -> Column(Modifier.fillMaxSize().padding(innerPadding)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
                    LocationFilter.entries.forEach { option ->
                        FilterChip(
                            selected = filter == option,
                            onClick = { viewModel.setFilter(option) },
                            label = { Text(when (option) { LocationFilter.ALL -> "Todas"; LocationFilter.FAVORITES -> "Favoritas"; LocationFilter.NOT_FAVORITES -> "No favoritas" }) },
                            leadingIcon = when (option) {
                                LocationFilter.FAVORITES -> { { Icon(Icons.Filled.Favorite, contentDescription = null) } }
                                LocationFilter.NOT_FAVORITES -> { { Icon(Icons.Outlined.FavoriteBorder, contentDescription = null) } }
                                LocationFilter.ALL -> null
                            },
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }
                if (locations.isEmpty()) EmptyLayout("No hay elementos a mostrar", Modifier.weight(1f)) else LazyColumn(Modifier.weight(1f)) {
                items(locations, key = { it.id }) { location ->
                    LocationRow(location, onClick = { onLocationClick(location.id) }, onFavoriteClick = { viewModel.toggleFavorite(location) })
                    HorizontalDivider()
                }
                }
            }
        }
    }
}

@Composable
private fun LocationRow(location: Location, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(Modifier.weight(1f)) {
        Text(location.name, style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface)
        Text(location.type, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onFavoriteClick) { Icon(if (location.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, "Favorito", tint = if (location.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}
