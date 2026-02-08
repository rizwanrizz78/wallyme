package io.wally.me.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class WallpaperTest {
    @Test
    fun testWallpaperCreation() {
        val wallpaper = Wallpaper(
            id = "1",
            title = "Test Wallpaper",
            category = "Abstract",
            views = 10,
            downloads = 5
        )

        assertEquals("1", wallpaper.id)
        assertEquals("Test Wallpaper", wallpaper.title)
        assertEquals("Abstract", wallpaper.category)
        assertEquals(10, wallpaper.views)
        assertEquals(5, wallpaper.downloads)
    }
}
