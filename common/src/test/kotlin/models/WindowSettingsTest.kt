package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class WindowSettingsTest {

    @Test
    fun testConstructor() {
        val expectedPosition = 0.0
        val expectedSize = 500.0

        val windowSettings = WindowSettings(expectedPosition, expectedPosition, expectedSize, expectedSize)
        assertEquals(windowSettings.y, expectedPosition)
        assertEquals(windowSettings.x, expectedPosition)
        assertEquals(windowSettings.height, expectedSize)
        assertEquals(windowSettings.width, expectedSize)
    }
}
