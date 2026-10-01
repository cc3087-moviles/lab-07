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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import uvg.moviles.lab_07.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersListViewModel = viewModel()
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Characters") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            items(viewModel.characters, key = { it.id }) { character ->
                CharacterRow(character, onClick = { onCharacterClick(character.id) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun CharacterRow(character: Character, onClick: () -> Unit) {
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
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(character.name, style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface)
            Text("${character.species} - ${character.status}", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(character.gender, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
