package models

import kotlinx.serialization.Serializable

/**
 *  Label which can be assigned to an item.
 *
 *  Value passed for label's id will be ignored. This property is managed by the database.
 */

@Serializable
data class Label(var name: String, var color: String, var id: Int = 0) {
    /*
        Two labels are said to be equal if they have the same id.
     */
    override fun equals(other: Any?): Boolean {
        return other is Label && other.id == this.id
    }

    override fun hashCode(): Int {
        return id
    }
}
