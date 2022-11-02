package todo.database.tables

import org.jetbrains.exposed.sql.Table

/**
 *  Intermediate database table to represent the many-to-many relationship between filters and labels.
 */
object FilterLabels : Table() {
    val filter = reference("filter", Filters)
    val label = reference("label", Labels)
}
