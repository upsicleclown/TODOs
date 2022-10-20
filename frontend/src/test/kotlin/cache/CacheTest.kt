package cache

import models.WindowSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class CacheTest {
    private val cache = Cache(this.javaClass.classLoader.getResource("cache/window_settings.json")!!.path)

    /*
       Tests related to window settings.
     */
    @Test
    fun testEditWindowSettings() {
        val newX = 419.0
        val newY = 70.0
        val newHeight = 600.0
        val newWidth = 400.0

        cache.editWindowSettings(WindowSettings(newX, newY, newHeight, newWidth))
        val newWindowSettings = cache.getWindowSettings()
        assertEquals(newWindowSettings.x, newX)
        assertEquals(newWindowSettings.y, newY)
        assertEquals(newWindowSettings.height, newHeight)
        assertEquals(newWindowSettings.width, newWidth)
    }

    @Test
    fun testGetWindowSettings() {
        val expectedPosition = 0.0
        val expectedSize = 500.0

        val windowSettings = cache.getWindowSettings()
        assertEquals(windowSettings.x, expectedPosition)
        assertEquals(windowSettings.y, expectedPosition)
        assertEquals(windowSettings.height, expectedSize)
        assertEquals(windowSettings.width, expectedSize)
    }
}
