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

    fun addWallpaper(title: String, category: String, imageData: ByteArray) {
        viewModelScope.launch {
            val wallpaper = Wallpaper(
                title = title,
                category = category
            )
            repository.addWallpaper(wallpaper, imageData)
        }
    }

    fun deleteWallpaper(id: String) {
        viewModelScope.launch {
            repository.deleteWallpaper(id)
        }
    }

    fun addCategory(name: String, imageData: ByteArray) {
        viewModelScope.launch {
            val category = Category(name = name)
            repository.addCategory(category, imageData)
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            repository.deleteCategory(id)
        }
    }
}
