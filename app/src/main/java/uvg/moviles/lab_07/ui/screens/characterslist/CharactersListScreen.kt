package uvg.moviles.lab_07.ui.screens.characterslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import uvg.moviles.lab_07.appViewModelFactory
import coil3.compose.AsyncImage
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.ui.components.ErrorLayout
import uvg.moviles.lab_07.ui.components.LoadingLayout
import uvg.moviles.lab_07.ui.components.EmptyLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersListViewModel = viewModel(factory = appViewModelFactory)
) {
    val state by viewModel.uiState.collectAsState()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsState()
    val characters = if (showOnlyFavorites) state.data.filter { it.isFavorite } else state.data

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Characters") },
            actions = {
                IconButton(onClick = viewModel::toggleFavoritesFilter) {
                    Icon(
                        imageVector = if (showOnlyFavorites) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Mostrar solo favoritos",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
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
                message = "Error al obtener listado de personajes. Intenta de nuevo",
                onRetry = viewModel::retry,
                modifier = Modifier.padding(innerPadding)
            )
            characters.isEmpty() -> EmptyLayout(
                message = "No hay elementos a mostrar",
                modifier = Modifier.padding(innerPadding)
            )
            else -> LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                items(characters, key = { it.id }) { character ->
                    CharacterRow(
                        character = character,
                        onClick = { onCharacterClick(character.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(character) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun CharacterRow(character: Character, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp).clip(CircleShape)
        )
        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(character.name, style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface)
            Text("${character.species} - ${character.status}", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(character.gender, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (character.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorito",
                tint = if (character.isFavorite) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
