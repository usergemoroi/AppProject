package com.bombaday.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bombaday.app.ui.screens.CameraScreen
import com.bombaday.app.ui.screens.FeedScreen
import com.bombaday.app.ui.screens.ProfileScreen
import com.bombaday.app.ui.theme.BombaDayTheme
import com.bombaday.app.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            BombaDayTheme {
                MainScreen(viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Feed : Screen("feed", "Лента", Icons.Default.Home)
    object Camera : Screen("camera", "Бомба", Icons.Default.Camera)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    val screens = listOf(
        Screen.Feed,
        Screen.Camera,
        Screen.Profile
    )
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
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
        },
        snackbarHost = {
            if (errorMessage != null) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(errorMessage ?: "")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Feed.route) {
                FeedScreen(viewModel = viewModel)
            }
            composable(Screen.Camera.route) {
                CameraScreen(viewModel = viewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel = viewModel)
            }
        }
    }
}
