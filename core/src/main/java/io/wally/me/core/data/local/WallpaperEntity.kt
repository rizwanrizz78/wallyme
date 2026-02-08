package io.wally.me.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.wally.me.core.model.Wallpaper

@Entity(tableName = "wallpapers")
data class WallpaperEntity(
    @PrimaryKey val id: String,
    val url: String,
    val title: String,
    val category: String,
    val views: Long,
    val downloads: Long,
    val timestamp: Long
)

fun WallpaperEntity.toDomain() = Wallpaper(
    id = id,
    url = url,
    title = title,
    category = category,
    views = views,
    downloads = downloads,
    timestamp = timestamp
)

fun Wallpaper.toEntity() = WallpaperEntity(
    id = id,
    url = url,
    title = title,
    category = category,
    views = views,
    downloads = downloads,
    timestamp = timestamp
)
