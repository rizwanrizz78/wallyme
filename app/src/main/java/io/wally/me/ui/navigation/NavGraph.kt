package io.wally.me.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import io.wally.me.ui.detail.DetailScreen
import io.wally.me.ui.favorites.FavoritesScreen
import io.wally.me.ui.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onWallpaperClick = { wallpaperId ->
                    navController.navigate(Screen.Detail.createRoute(wallpaperId))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("wallpaperId") { type = NavType.StringType })
        ) {
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onWallpaperClick = { wallpaperId ->
                    navController.navigate(Screen.Detail.createRoute(wallpaperId))
                }
            )
        }
    }
}
