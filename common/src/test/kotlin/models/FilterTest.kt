package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class FilterTest {

    @Test
    fun testConstructor() {
        val expectedEdtStartDate = LocalDateTime.parse("2010-06-01T22:19:44")
        val expectedEdtEndDate = LocalDateTime.parse("2016-06-01T22:19:44")
        val expectedLabelBooleanOperator = BooleanOperator.AND
        val expectedIsCompleted = false

        val filter = Filter(expectedEdtStartDate, expectedEdtEndDate, expectedIsCompleted, labelBooleanOperator = expectedLabelBooleanOperator)
        assertEquals(filter.edtStartDateRange, expectedEdtStartDate)
        assertEquals(filter.edtEndDateRange, expectedEdtEndDate)
        assertEquals(filter.edtEndDateRange, expectedEdtEndDate)
        assertEquals(filter.labelBooleanOperator, expectedLabelBooleanOperator)
        assertEquals(filter.labelIds, listOf<Int>())
        assertEquals(filter.priorities, listOf<Priority>())
    }
}
