package uvg.moviles.lab_07.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.moviles.lab_07.R
import uvg.moviles.lab_07.appViewModelFactory

@Composable
fun LoginScreen(
    onLoginCompleted: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = appViewModelFactory)
) {
    val name by viewModel.name.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(72.dp))
            Image(
                painter = painterResource(id = R.drawable.rick_and_morty_logo),
                contentDescription = "Logo de Rick and Morty",
                modifier = Modifier.fillMaxWidth().height(160.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
            OutlinedTextField(
                value = name,
                onValueChange = viewModel::onNameChanged,
                label = { Text("Nombre") },
                placeholder = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.login(onLoginCompleted) },
                enabled = name.trim().isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) { Text(text = "Iniciar sesión") }
        }
    }
}
