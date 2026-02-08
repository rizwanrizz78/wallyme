package io.wally.me.core.model

data class Wallpaper(
    val id: String = "",
    val url: String = "",
    val title: String = "",
    val category: String = "",
    val views: Long = 0,
    val downloads: Long = 0,
    val timestamp: Long = System.currentTimeMillis()
)
