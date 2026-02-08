package io.wally.me.admin.ui.wallpaper

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.wally.me.admin.ui.AdminViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWallpaperScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onWallpaperAdded: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is AdminViewModel.UiState.Success) {
            val state = uiState as AdminViewModel.UiState.Success
            snackbarHostState.showSnackbar(state.message)
            viewModel.resetState()
            // Delay navigation to let snackbar show
            // onWallpaperAdded()
        } else if (uiState is AdminViewModel.UiState.Error) {
            val state = uiState as AdminViewModel.UiState.Error
            snackbarHostState.showSnackbar(state.message)
            viewModel.resetState()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Wallpaper") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState is AdminViewModel.UiState.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text(if (imageUri == null) "Select Image" else "Change Image")
            }

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Category Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    label = { Text("Category") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().clickable { expanded = true }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name) },
                            onClick = {
                                category = cat.name
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (imageUri != null && title.isNotEmpty() && category.isNotEmpty()) {
                        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri!!)
                        val bytes = inputStream?.readBytes()
                        if (bytes != null) {
                            viewModel.addWallpaper(title, category, bytes)
                            onWallpaperAdded()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is AdminViewModel.UiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = io.wally.me.admin.ui.theme.NeonBlue,
                    contentColor = Color.Black
                )
            ) {
                Text("Add Wallpaper")
            }
        }
    }
}
