package models

import kotlinx.serialization.Serializable

/**
 *  Group which is based on a filter
 *
 *  Value passed for group's id will be ignored. This property is managed by the database.
 */
@Serializable
data class Group(var name: String, var filter: Filter, var id: Int = 0) {
    /*
        Two groups are said to be equal if they have the same id.
     */
    override fun equals(other: Any?): Boolean {
        return other is Group && other.id == this.id
    }

    override fun hashCode(): Int {
        return id
    }
}
