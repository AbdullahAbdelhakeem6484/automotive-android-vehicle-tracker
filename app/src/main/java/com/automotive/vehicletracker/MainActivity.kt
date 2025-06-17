package com.automotive.vehicletracker

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.automotive.vehicletracker.ui.screens.dashboard.DashboardScreen
import com.automotive.vehicletracker.ui.screens.maps.MapsScreen
import com.automotive.vehicletracker.ui.screens.triplist.TripListScreen
import com.automotive.vehicletracker.ui.screens.tripdetail.TripDetailScreen
import com.automotive.vehicletracker.ui.screens.settings.SettingsScreen
import com.automotive.vehicletracker.ui.theme.VehicleTrackerTheme

// Navigation destinations
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Maps : Screen("maps", "Maps", Icons.Default.Map)
    object TripList : Screen("triplist", "Trips", Icons.AutoMirrored.Filled.List)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object TripDetail : Screen("tripdetail/{tripId}", "Trip Detail", Icons.Default.Info) {
        fun createRoute(tripId: Long) = "tripdetail/$tripId"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val isAutomotive = packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)
        
        setContent {
            VehicleTrackerTheme {
                VehicleTrackerApp(isAutomotive = isAutomotive)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleTrackerApp(isAutomotive: Boolean) {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        Screen.Dashboard,
        Screen.Maps,
        Screen.TripList,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                screen.icon, 
                                contentDescription = screen.title,
                                modifier = Modifier.size(24.dp)
                            ) 
                        },
                        label = { 
                            Text(
                                screen.title,
                                style = MaterialTheme.typography.labelSmall
                            ) 
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToMaps = { navController.navigate(Screen.Maps.route) },
                    onNavigateToTripList = { navController.navigate(Screen.TripList.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            
            composable(Screen.Maps.route) {
                MapsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(Screen.TripList.route) {
                TripListScreen(
                    onNavigateToTripDetail = { tripId ->
                        navController.navigate(Screen.TripDetail.createRoute(tripId))
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(Screen.TripDetail.route) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId")?.toLongOrNull() ?: 0L
                TripDetailScreen(
                    tripId = tripId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}