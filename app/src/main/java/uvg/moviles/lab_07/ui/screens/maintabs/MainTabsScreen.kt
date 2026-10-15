package uvg.moviles.lab_07.ui.screens.maintabs

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import uvg.moviles.lab_07.navigation.CharacterDetailDestination
import uvg.moviles.lab_07.navigation.CharactersGraph
import uvg.moviles.lab_07.navigation.CharactersListDestination
import uvg.moviles.lab_07.navigation.LocationDetailDestination
import uvg.moviles.lab_07.navigation.LocationsGraph
import uvg.moviles.lab_07.navigation.LocationsListDestination
import uvg.moviles.lab_07.navigation.ProfileDestination
import uvg.moviles.lab_07.ui.screens.characterdetail.CharacterDetailScreen
import uvg.moviles.lab_07.ui.screens.characterslist.CharactersListScreen
import uvg.moviles.lab_07.ui.screens.locationdetail.LocationDetailScreen
import uvg.moviles.lab_07.ui.screens.locationslist.LocationsListScreen
import uvg.moviles.lab_07.ui.screens.profile.ProfileScreen

private enum class Tab { CHARACTERS, LOCATIONS, PROFILE }

@Composable
fun MainTabsScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination.isInGraph<CharactersGraph>(),
                    onClick = { navController.navigateToTab(Tab.CHARACTERS) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Characters") },
                    label = { Text("Characters") }
                )
                NavigationBarItem(
                    selected = currentDestination.isInGraph<LocationsGraph>(),
                    onClick = { navController.navigateToTab(Tab.LOCATIONS) },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Locations") },
                    label = { Text("Locations") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<ProfileDestination>() == true,
                    onClick = { navController.navigateToTab(Tab.PROFILE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<CharactersGraph>(startDestination = CharactersListDestination) {
                composable<CharactersListDestination> {
                    CharactersListScreen(
                        onCharacterClick = { characterId ->
                            navController.navigate(CharacterDetailDestination(characterId))
                        }
                    )
                }
                composable<CharacterDetailDestination> {
                    CharacterDetailScreen(onBackClick = navController::popBackStack)
                }
            }
            navigation<LocationsGraph>(startDestination = LocationsListDestination) {
                composable<LocationsListDestination> {
                    LocationsListScreen(
                        onLocationClick = { locationId ->
                            navController.navigate(LocationDetailDestination(locationId))
                        }
                    )
                }
                composable<LocationDetailDestination> {
                    LocationDetailScreen(onBackClick = navController::popBackStack)
                }
            }
            composable<ProfileDestination> {
                ProfileScreen(onLogoutCompleted = onLogout)
            }
        }
    }
}

private fun NavController.navigateToTab(tab: Tab) {
    val startDestinationId = graph.findStartDestination().id
    when (tab) {
        Tab.CHARACTERS -> navigate(CharactersGraph) { preserveTabState(startDestinationId) }
        Tab.LOCATIONS -> navigate(LocationsGraph) { preserveTabState(startDestinationId) }
        Tab.PROFILE -> navigate(ProfileDestination) { preserveTabState(startDestinationId) }
    }
}

private fun androidx.navigation.NavOptionsBuilder.preserveTabState(startDestinationId: Int) {
    popUpTo(startDestinationId) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

private inline fun <reified T : Any> androidx.navigation.NavDestination?.isInGraph(): Boolean {
    return this?.hierarchy?.any { it.hasRoute<T>() } == true
}
