package models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 *  Individual TO-DO item. An item can have multiple labels.
 *
 *  Value passed for edtDueDate must be in ISO-8601 format:
 *      (https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME)
 *  Value passed for item's id will be ignored. This property is managed by the database.
 */
@Serializable
data class Item(
    var title: String,
    var isCompleted: Boolean,
    var labelIds: MutableList<Int> = mutableListOf(),
    var edtDueDate: LocalDateTime? = null,
    var id: Int = 0
) {
    /*
        Two items are said to be equal if they have the same id.
     */
    override fun equals(other: Any?): Boolean {
        return other is Item && other.id == this.id
    }

    override fun hashCode(): Int {
        return id
    }
}
