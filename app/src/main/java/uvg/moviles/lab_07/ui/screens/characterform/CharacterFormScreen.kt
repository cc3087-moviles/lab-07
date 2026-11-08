package uvg.moviles.lab_07.ui.screens.characterform

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.moviles.lab_07.appViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(onBackClick: () -> Unit, onSaved: (Int) -> Unit, viewModel: CharacterFormViewModel = viewModel(factory = appViewModelFactory)) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0), topBar = { TopAppBar(title = { Text(if (viewModel.isEditing) "Edit character" else "Create character") }, navigationIcon = { IconButton(onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary, navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)) }) { padding ->
        if (state.isLoading) CircularProgressIndicator(Modifier.padding(padding).padding(32.dp)) else Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            OutlinedTextField(state.name, { value -> viewModel.update { it.copy(name = value) } }, label = { Text("Nombre") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(state.species, { value -> viewModel.update { it.copy(species = value) } }, label = { Text("Especie") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            ChoiceField("Status", state.status, listOf("Alive", "Dead", "unknown")) { value -> viewModel.update { it.copy(status = value) } }
            ChoiceField("Género", state.gender, listOf("Male", "Female", "Genderless", "unknown")) { value -> viewModel.update { it.copy(gender = value) } }
            OutlinedTextField(state.image, { value -> viewModel.update { it.copy(image = value) } }, label = { Text("URL de imagen") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            Button(onClick = { viewModel.save(onSaved) }, enabled = state.name.trim().isNotEmpty() && state.species.trim().isNotEmpty(), modifier = Modifier.fillMaxWidth()) { Text("Guardar") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun ChoiceField(label: String, selected: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
        OutlinedTextField(selected, {}, readOnly = true, label = { Text(label) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
        ExposedDropdownMenu(expanded, { expanded = false }) { options.forEach { option -> DropdownMenuItem({ Text(option) }, { onSelected(option); expanded = false }) } }
    }
}
