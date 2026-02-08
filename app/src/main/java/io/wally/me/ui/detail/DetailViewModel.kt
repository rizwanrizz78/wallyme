package io.wally.me.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.wally.me.core.data.repository.WallpaperRepository
import io.wally.me.core.model.Wallpaper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wallpaperId: String = checkNotNull(savedStateHandle["wallpaperId"])

    val wallpaper: StateFlow<Wallpaper?> = repository.getWallpaper(wallpaperId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isFavorite: StateFlow<Boolean> = repository.isFavorite(wallpaperId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun incrementViewCount() {
        viewModelScope.launch {
            repository.incrementViewCount(wallpaperId)
        }
    }

    fun incrementDownloadCount() {
        viewModelScope.launch {
            repository.incrementDownloadCount(wallpaperId)
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentWallpaper = wallpaper.value ?: return@launch
            if (isFavorite.value) {
                repository.removeFromFavorites(currentWallpaper)
            } else {
                repository.addToFavorites(currentWallpaper)
            }
        }
    }
}
