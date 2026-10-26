package uvg.moviles.lab_07.ui.screens.characterdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import uvg.moviles.lab_07.appViewModelFactory
import uvg.moviles.lab_07.ui.components.ErrorLayout
import uvg.moviles.lab_07.ui.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CharacterDetailViewModel = viewModel(factory = appViewModelFactory)
) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Characters details") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            },
            actions = {
                state.data?.let { character ->
                    IconButton(onClick = { viewModel.toggleFavorite(character) }) {
                        Icon(
                            imageVector = if (character.isFavorite) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (character.isFavorite) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { innerPadding ->
        when {
            state.isLoading -> LoadingLayout(
                onClick = viewModel::onLoadingClicked,
                modifier = Modifier.padding(innerPadding)
            )
            state.hasError -> ErrorLayout(
                message = "Error al obtener el personaje. Intenta de nuevo",
                onRetry = viewModel::retry,
                modifier = Modifier.padding(innerPadding)
            )
            else -> {
                val character = state.data ?: return@Scaffold
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(character.image, character.name, contentScale = ContentScale.Crop,
                        modifier = Modifier.size(160.dp).clip(CircleShape))
                    Text(character.name, style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp, bottom = 24.dp))
                    DetailRow("Species:", character.species)
                    HorizontalDivider()
                    DetailRow("Status:", character.status)
                    HorizontalDivider()
                    DetailRow("Gender:", character.gender)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}
