package io.wally.me.admin.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.wally.me.core.data.repository.WallpaperRepository
import io.wally.me.core.model.Category
import io.wally.me.core.model.Wallpaper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {

    val wallpapers: StateFlow<List<Wallpaper>> = repository.getWallpapers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<Category>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = kotlinx.coroutines.flow.MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val message: String) : UiState()
        data class Error(val message: String) : UiState()
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }

    fun addWallpaper(title: String, category: String, imageData: ByteArray) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val wallpaper = Wallpaper(
                title = title,
                category = category
            )
            val result = repository.addWallpaper(wallpaper, imageData)
            if (result.isSuccess) {
                _uiState.value = UiState.Success("Wallpaper added successfully")
            } else {
                _uiState.value = UiState.Error("Failed to add wallpaper: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun deleteWallpaper(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.deleteWallpaper(id)
             if (result.isSuccess) {
                _uiState.value = UiState.Success("Wallpaper deleted")
            } else {
                _uiState.value = UiState.Error("Failed to delete: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun addCategory(name: String, imageData: ByteArray) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val category = Category(name = name)
            val result = repository.addCategory(category, imageData)
             if (result.isSuccess) {
                _uiState.value = UiState.Success("Category added successfully")
            } else {
                _uiState.value = UiState.Error("Failed to add category: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.deleteCategory(id)
             if (result.isSuccess) {
                _uiState.value = UiState.Success("Category deleted")
            } else {
                _uiState.value = UiState.Error("Failed to delete: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun testConnection() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.testFirestoreConnection()
            if (result.isSuccess) {
                _uiState.value = UiState.Success("Firestore Connection Successful!")
            } else {
                _uiState.value = UiState.Error("Connection Failed: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
