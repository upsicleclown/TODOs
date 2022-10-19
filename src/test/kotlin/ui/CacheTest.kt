package ui

import models.WindowSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class CacheTest {
    private val cache = Cache()

    /*
       Tests related to window settings.
     */
    @Test
    fun testEditWindowSettings() {
        val newHeight = 600.0
        val newWidth = 400.0

        cache.editWindowSettings(WindowSettings(newHeight, newWidth))
        val newWindowSettings = cache.getWindowSettings()
        assertEquals(newWindowSettings.height, newHeight)
        assertEquals(newWindowSettings.width, newWidth)
    }

    @Test
    fun testGetWindowSettings() {
        val expectedValue = 500.0

        val windowSettings = cache.getWindowSettings()
        assertEquals(windowSettings.height, expectedValue)
        assertEquals(windowSettings.width, expectedValue)
    }
}
