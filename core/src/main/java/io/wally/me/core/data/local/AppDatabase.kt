package io.wally.me.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.wally.me.core.model.Wallpaper

@Database(entities = [WallpaperEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wallpaperDao(): WallpaperDao
}
