package io.wally.me.admin.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.wally.me.admin.ui.AdminViewModel
import io.wally.me.core.model.Category
import io.wally.me.core.model.Wallpaper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onAddWallpaperClick: () -> Unit,
    onAddCategoryClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val wallpapers by viewModel.wallpapers.collectAsState()
    val categories by viewModel.categories.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Dashboard") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (selectedTab == 0) onAddWallpaperClick() else onAddCategoryClick()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Text("Wallpapers") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Text("Categories") }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (selectedTab == 0) {
                LazyColumn {
                    items(wallpapers) { wallpaper ->
                        WallpaperListItem(wallpaper, onDelete = { viewModel.deleteWallpaper(wallpaper.id) })
                    }
                }
            } else {
                LazyColumn {
                    items(categories) { category ->
                        CategoryListItem(category, onDelete = { viewModel.deleteCategory(category.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun WallpaperListItem(wallpaper: Wallpaper, onDelete: () -> Unit) {
    ListItem(
        headlineContent = { Text(wallpaper.title) },
        supportingContent = { Text(wallpaper.category) },
        leadingContent = {
            AsyncImage(
                model = wallpaper.url,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    )
}

@Composable
fun CategoryListItem(category: Category, onDelete: () -> Unit) {
    ListItem(
        headlineContent = { Text(category.name) },
        leadingContent = {
            AsyncImage(
                model = category.coverUrl,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    )
}
