package uvg.moviles.lab_07.ui.screens.startup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.moviles.lab_07.appViewModelFactory

@Composable
fun StartupScreen(
    onSessionResolved: (Boolean) -> Unit,
    viewModel: StartupViewModel = viewModel(factory = appViewModelFactory)
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        StartupState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        is StartupState.SessionResolved -> LaunchedEffect(currentState.hasActiveSession) {
            onSessionResolved(currentState.hasActiveSession)
        }
    }
}
