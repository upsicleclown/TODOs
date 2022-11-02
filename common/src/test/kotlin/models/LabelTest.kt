package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LabelTest {

    @Test
    fun testConstructor() {
        val expectedName = "testLabel"
        val expectedColor = "#FFC0CB"

        val label = Label(expectedName, expectedColor)
        assertEquals(label.name, expectedName, expectedColor)
    }
}
