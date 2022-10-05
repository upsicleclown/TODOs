package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GroupTest {

    @Test
    fun testConstructor() {
        val expectedName = "testGroup"

        val group = Group(expectedName)
        assertEquals(group.name, expectedName)
        assertEquals(group.labelIds, listOf<Int>())
    }
}