package com.selfcare.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.selfcare.tracker.ui.LocalTranslations
import com.selfcare.tracker.ui.I18n.zhTranslations
import com.selfcare.tracker.ui.I18n.enTranslations
import com.selfcare.tracker.ui.screens.*
import com.selfcare.tracker.ui.theme.SelfCareTheme
import com.selfcare.tracker.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector)
val screens = listOf(
    Screen("add", Icons.Default.Add),
    Screen("records", Icons.Default.List),
    Screen("stats", Icons.Default.BarChart),
    Screen("settings", Icons.Default.Settings)
)

@Composable
fun MainApp() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsState by settingsViewModel.uiState.collectAsState()

    val translations = if (settingsState.lang == "zh") zhTranslations else enTranslations
    val isDarkTheme = settingsState.theme == "dark"

    SelfCareTheme(darkTheme = isDarkTheme) {
        CompositionLocalProvider(LocalTranslations provides translations) {
            val navController = rememberNavController()
            val t = LocalTranslations.current

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        screens.forEach { screen ->
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = null) },
                                label = {
                                    Text(
                                        when (screen.route) {
                                            "add" -> t.tabAdd
                                            "records" -> t.tabRecords
                                            "stats" -> t.tabStats
                                            "settings" -> t.tabSettings
                                            else -> ""
                                        }
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
                    startDestination = settingsState.uiConfig.defaultHome.ifEmpty { "add" },
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("add") { AddScreen() }
                    composable("records") { RecordsScreen() }
                    composable("stats") { StatsScreen() }
                    composable("settings") { SettingsScreen(viewModel = settingsViewModel) }
                }
            }
        }
    }
}
