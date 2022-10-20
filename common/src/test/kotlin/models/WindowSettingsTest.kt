package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class WindowSettingsTest {

    @Test
    fun testConstructor() {
        val expectedValue = 500.0

        val windowSettings = WindowSettings(expectedValue, expectedValue)
        assertEquals(windowSettings.height, expectedValue)
        assertEquals(windowSettings.width, expectedValue)
    }
}
