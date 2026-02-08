package io.wally.me.admin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.wally.me.admin.ui.dashboard.DashboardScreen
import io.wally.me.admin.ui.wallpaper.AddWallpaperScreen
import io.wally.me.admin.ui.category.AddCategoryScreen

@Composable
fun AdminNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AdminScreen.Dashboard.route
    ) {
        composable(AdminScreen.Dashboard.route) {
            DashboardScreen(
                onAddWallpaperClick = { navController.navigate(AdminScreen.AddWallpaper.route) },
                onAddCategoryClick = { navController.navigate(AdminScreen.AddCategory.route) }
            )
        }

        composable(AdminScreen.AddWallpaper.route) {
            AddWallpaperScreen(
                onWallpaperAdded = { navController.popBackStack() }
            )
        }

        composable(AdminScreen.AddCategory.route) {
            AddCategoryScreen(
                onCategoryAdded = { navController.popBackStack() }
            )
        }
    }
}
