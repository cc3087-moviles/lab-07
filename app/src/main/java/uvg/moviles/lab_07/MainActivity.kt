package uvg.moviles.lab_07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uvg.moviles.lab_07.navigation.Login
import uvg.moviles.lab_07.navigation.MainTabs
import uvg.moviles.lab_07.ui.screens.login.LoginScreen
import uvg.moviles.lab_07.ui.screens.maintabs.MainTabsScreen
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
                    // Al navegar al contenedor principal, se elimina Login del back stack
                    // (inclusive = true). Así, si dentro de los tabs se presiona
                    // "back", ya no hay nada a donde regresar y la app se
                    // cierra, en vez de volver al Login.
                    navController.navigate(MainTabs) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        composable<MainTabs> {
            MainTabsScreen(
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
