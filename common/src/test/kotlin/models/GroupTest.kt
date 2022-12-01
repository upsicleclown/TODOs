package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GroupTest {

    @Test
    fun testConstructor() {
        val expectedName = "testGroup"
        val expectedLabelBooleanOperator = BooleanOperator.AND
        val expectedFilter = Filter(labelBooleanOperator = expectedLabelBooleanOperator)

        val group = Group(expectedName, expectedFilter)
        assertEquals(group.name, expectedName)
        assertEquals(group.filter, expectedFilter)
    }
}
