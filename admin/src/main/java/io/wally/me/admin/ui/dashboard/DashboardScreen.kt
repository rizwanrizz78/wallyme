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
import androidx.compose.ui.graphics.Color
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
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is AdminViewModel.UiState.Success) {
            val state = uiState as AdminViewModel.UiState.Success
            snackbarHostState.showSnackbar(state.message)
            viewModel.resetState()
        } else if (uiState is AdminViewModel.UiState.Error) {
            val state = uiState as AdminViewModel.UiState.Error
            snackbarHostState.showSnackbar(state.message)
            viewModel.resetState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard", color = io.wally.me.admin.ui.theme.NeonBlue) },
                actions = {
                    TextButton(onClick = { viewModel.testConnection() }) {
                         Text("Test DB", color = io.wally.me.admin.ui.theme.NeonPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) onAddWallpaperClick() else onAddCategoryClick()
                },
                containerColor = io.wally.me.admin.ui.theme.NeonBlue,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = io.wally.me.admin.ui.theme.DeepSpaceBlack) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Text("Wallpapers") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = io.wally.me.admin.ui.theme.NeonBlue,
                        indicatorColor = io.wally.me.admin.ui.theme.NeonBlue.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Text("Categories") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = io.wally.me.admin.ui.theme.NeonPurple,
                        indicatorColor = io.wally.me.admin.ui.theme.NeonPurple.copy(alpha = 0.2f)
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (selectedTab == 0) {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(wallpapers) { wallpaper ->
                        WallpaperListItem(wallpaper, onDelete = { viewModel.deleteWallpaper(wallpaper.id) })
                    }
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
    io.wally.me.admin.ui.components.GlassBox(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { Text(wallpaper.title, color = io.wally.me.admin.ui.theme.NeonBlue) },
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
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = io.wally.me.admin.ui.theme.NeonPink)
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}

@Composable
fun CategoryListItem(category: Category, onDelete: () -> Unit) {
    io.wally.me.admin.ui.components.GlassBox(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { Text(category.name, color = io.wally.me.admin.ui.theme.NeonPurple) },
            leadingContent = {
                AsyncImage(
                    model = category.coverUrl,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            },
            trailingContent = {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = io.wally.me.admin.ui.theme.NeonPink)
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}
