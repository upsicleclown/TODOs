package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LabelTest {

    @Test
    fun testConstructor() {
        val expectedName = "testLabel"

        val label = Label(expectedName)
        assertEquals(label.name, expectedName)
    }
}