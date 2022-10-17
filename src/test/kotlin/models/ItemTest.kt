package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ItemTest {

    @Test
    fun testConstructor() {
        val expectedTitle = "testTitle"
        val expectedIsCompleted = false

        val item = Item(expectedTitle, expectedIsCompleted)
        assertEquals(item.title, expectedTitle)
        assertEquals(item.isCompleted, expectedIsCompleted)
        assertEquals(item.labelIds, listOf<Int>())
    }
}
