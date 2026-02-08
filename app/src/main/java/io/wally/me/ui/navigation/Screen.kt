package io.wally.me.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{wallpaperId}") {
        fun createRoute(wallpaperId: String) = "detail/$wallpaperId"
    }
    object Favorites : Screen("favorites")
}
