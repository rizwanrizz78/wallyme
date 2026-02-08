package io.wally.me.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {
    @Query("SELECT * FROM wallpapers ORDER BY timestamp DESC")
    fun getFavorites(): Flow<List<WallpaperEntity>>

    @Query("SELECT * FROM wallpapers WHERE id = :id")
    suspend fun getWallpaper(id: String): WallpaperEntity?

    @Query("SELECT EXISTS(SELECT * FROM wallpapers WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallpaper: WallpaperEntity)

    @Delete
    suspend fun delete(wallpaper: WallpaperEntity)
}
