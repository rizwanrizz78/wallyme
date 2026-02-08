package io.wally.me.core.data.repository

import io.wally.me.core.model.Category
import io.wally.me.core.model.Wallpaper
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    fun getWallpapers(): Flow<List<Wallpaper>>
    fun getWallpaper(id: String): Flow<Wallpaper?>
    fun getCategories(): Flow<List<Category>>

    // Admin functions
    suspend fun addWallpaper(wallpaper: Wallpaper, imageData: ByteArray): Result<Unit>
    suspend fun updateWallpaper(wallpaper: Wallpaper): Result<Unit>
    suspend fun deleteWallpaper(id: String): Result<Unit>
    suspend fun addCategory(category: Category, imageData: ByteArray): Result<Unit>
    suspend fun deleteCategory(id: String): Result<Unit>

    // User interactions
    suspend fun incrementViewCount(id: String)
    suspend fun incrementDownloadCount(id: String)

    // Favorites
    fun getFavorites(): Flow<List<Wallpaper>>
    fun isFavorite(id: String): Flow<Boolean>
    suspend fun addToFavorites(wallpaper: Wallpaper)
    suspend fun removeFromFavorites(wallpaper: Wallpaper)

    // Debug
    suspend fun testFirestoreConnection(): Result<Unit>
}
