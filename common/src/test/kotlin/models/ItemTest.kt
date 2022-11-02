package models

import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ItemTest {

    @Test
    fun testConstructor() {
        val expectedTitle = "testTitle"
        val expectedIsCompleted = false
        val expectDate = "2010-06-01T22:19:44".toLocalDateTime()
        val expectedIsHighPriority = Priority.HIGH

        val item = Item(expectedTitle, expectedIsCompleted, edtDueDate = expectDate, priority = expectedIsHighPriority)
        assertEquals(item.title, expectedTitle)
        assertEquals(item.isCompleted, expectedIsCompleted)
        assertEquals(item.labelIds, listOf<Int>())
        assertEquals(item.edtDueDate, expectDate)
        assertEquals(item.priority, expectedIsHighPriority)
    }
}
