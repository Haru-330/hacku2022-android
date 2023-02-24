package jp.kusunoki.hacku2022_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.kusunoki.hacku2022_android.ui.component.pages.HistoryScreen
import jp.kusunoki.hacku2022_android.ui.component.pages.HomeScreen
import jp.kusunoki.hacku2022_android.ui.component.pages.VideoScreen
import jp.kusunoki.hacku2022_android.ui.theme.Hacku2022androidTheme
import timber.log.Timber

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No Current NavController")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hacku2022androidTheme {
                val navController = rememberNavController()

                CompositionLocalProvider(
                    LocalNavController provides navController
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            MainBottomNavigation()
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            NavHost(
                                navController = LocalNavController.current,
                                startDestination = Screen.Home.route
                            ) {
                                composable(Screen.Home.route) {
                                    Timber.d("MainActivityのHomeScreen")
                                    HomeScreen()
                                }
                                composable(Screen.History.route) {
                                    Timber.d("MainActivityのHistoryScreen")
                                    HistoryScreen()
                                }
                                composable(
                                    route = "${Screen.Video.route}/{videoUrl}",
                                ) { backStackEntry ->
                                    val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                                    VideoScreen(videoUrl)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector = Icons.Default.Home, @StringRes val resourceId: Int = R.string.app_name) {
    object Home: Screen("home", Icons.Default.Home, R.string.home_screen)
    object History: Screen("history", Icons.Default.List, R.string.history_screen)
    object Video: Screen("video")
}

@Composable
fun MainBottomNavigation() {
    val items = listOf(Screen.Home, Screen.History)
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val homeOrHistory = currentDestination?.hierarchy?.any {
        it.route == Screen.Home.route || it.route == Screen.History.route
    }

    if(homeOrHistory == true) {
        BottomNavigation {
            items.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(screen.icon, contentDescription = null)
                    },
                    label = {
                        Text(text = stringResource(id = screen.resourceId))
                    },
                    selected = currentDestination.hierarchy.any { it.route == screen.route },
                    onClick = {
                        navController.navigate(screen.route)
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Hacku2022androidTheme {
        Greeting("Android")
    }
}