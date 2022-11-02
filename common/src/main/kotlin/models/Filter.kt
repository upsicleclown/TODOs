package models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 *  Filter which is based on item attributes, labels and priorities
 *
 *  Value passed for filter's id will be ignored. This property is managed by the database.
 */
@Serializable
data class Filter(var edtStartDateRange: LocalDateTime? = null, var edtEndDateRange: LocalDateTime? = null, var isCompleted: Boolean? = null, var priorities: MutableList<Priority> = mutableListOf(), var labelIds: MutableList<Int> = mutableListOf(), var id: Int = 0) {
    /*
        Two filters are said to be equal if they have the same id.
     */
    override fun equals(other: Any?): Boolean {
        return other is Filter && other.id == this.id
    }

    override fun hashCode(): Int {
        return id
    }
}
