package uvg.moviles.lab_07.ui.screens.characterslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FilterChip
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import uvg.moviles.lab_07.appViewModelFactory
import coil3.compose.AsyncImage
import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.ui.components.ErrorLayout
import uvg.moviles.lab_07.ui.components.LoadingLayout
import uvg.moviles.lab_07.ui.components.EmptyLayout
import uvg.moviles.lab_07.ui.components.CharacterAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(
    onCharacterClick: (Int) -> Unit,
    onCreateCharacter: () -> Unit,
    viewModel: CharactersListViewModel = viewModel(factory = appViewModelFactory)
) {
    val state by viewModel.uiState.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val characters = when (filter) {
        CharacterFilter.ALL -> state.data
        CharacterFilter.FAVORITES -> state.data.filter { it.isFavorite }
        CharacterFilter.NOT_FAVORITES -> state.data.filterNot { it.isFavorite }
    }

    Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0), topBar = {
        TopAppBar(
            title = { Text("Characters") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = onCreateCharacter) {
            Icon(Icons.Default.Add, contentDescription = "Crear personaje")
        }
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
            else -> Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
                    CharacterFilter.entries.forEach { option ->
                        FilterChip(
                            selected = filter == option,
                            onClick = { viewModel.setFilter(option) },
                            label = { Text(when (option) { CharacterFilter.ALL -> "Todas"; CharacterFilter.FAVORITES -> "Favoritas"; CharacterFilter.NOT_FAVORITES -> "No favoritas" }) },
                            leadingIcon = when (option) {
                                CharacterFilter.FAVORITES -> { { Icon(Icons.Filled.Favorite, contentDescription = null) } }
                                CharacterFilter.NOT_FAVORITES -> { { Icon(Icons.Outlined.FavoriteBorder, contentDescription = null) } }
                                CharacterFilter.ALL -> null
                            },
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }
                if (characters.isEmpty()) {
                    EmptyLayout("No hay elementos a mostrar", Modifier.weight(1f))
                } else {
                    LazyColumn(Modifier.weight(1f)) {
                        items(characters, key = { it.id }) { character ->
                            CharacterRow(character, { onCharacterClick(character.id) }, { viewModel.toggleFavorite(character) })
                            HorizontalDivider()
                        }
                    }
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
        CharacterAvatar(character.image, character.name, Modifier.size(56.dp))
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
