package uvg.moviles.lab_07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uvg.moviles.lab_07.navigation.CharacterDetail
import uvg.moviles.lab_07.navigation.CharactersList
import uvg.moviles.lab_07.navigation.Login
import uvg.moviles.lab_07.ui.screens.CharacterDetailScreen
import uvg.moviles.lab_07.ui.screens.CharactersListScreen
import uvg.moviles.lab_07.ui.screens.LoginScreen
import uvg.moviles.lab_07.ui.theme.Lab07Theme

/**
 * Única Activity de la app. Toda la navegación sucede entre composables
 * usando Navigation-Compose con destinations tipados (@Serializable),
 * definidos en el paquete "navigation".
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab07Theme {
                RickAndMortyApp()
            }
        }
    }
}

@Composable
fun RickAndMortyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(
                onStartClick = {
                    // Al navegar a la lista, se elimina Login del back stack
                    // (inclusive = true). Así, si en Characters se presiona
                    // "back", ya no hay nada a donde regresar y la app se
                    // cierra, en vez de volver al Login.
                    navController.navigate(CharactersList) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        composable<CharactersList> {
            CharactersListScreen(
                onCharacterClick = { characterId ->
                    // Únicamente se manda el ID del personaje, nada más.
                    navController.navigate(CharacterDetail(characterId = characterId))
                }
            )
        }

        composable<CharacterDetail> {
            CharacterDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
