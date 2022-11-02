package todo.database.tables

import org.jetbrains.exposed.sql.Table

/**
 *  Intermediate database table to represent the many-to-many relationship between filters and labels.
 */
object FilterPriorities : Table() {
    val filter = reference("filter", Filters)
    val priority = reference("priority", Priorities)
}
