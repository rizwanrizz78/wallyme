package io.wally.me.admin.ui.navigation

sealed class AdminScreen(val route: String) {
    object Dashboard : AdminScreen("dashboard")
    object AddWallpaper : AdminScreen("add_wallpaper")
    object AddCategory : AdminScreen("add_category")
}
