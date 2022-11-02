package models

import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FilterTest {

    @Test
    fun testConstructor() {
        val expectedEdtStartDate = "2010-06-01T22:19:44".toLocalDateTime()
        val expectedEdtEndDate = "2016-06-01T22:19:44".toLocalDateTime()
        val expectedIsCompleted = false

        val filter = Filter(expectedEdtStartDate, expectedEdtEndDate, expectedIsCompleted)
        assertEquals(filter.edtStartDateRange, expectedEdtStartDate)
        assertEquals(filter.edtEndDateRange, expectedEdtEndDate)
        assertEquals(filter.edtEndDateRange, expectedEdtEndDate)
        assertEquals(filter.labelIds, listOf<Int>())
        assertEquals(filter.priorities, listOf<Priority>())
    }
}
