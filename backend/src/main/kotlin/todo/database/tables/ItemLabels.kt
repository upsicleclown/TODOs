package todo.database.tables

import org.jetbrains.exposed.sql.Table

/**
 *  Intermediate database table to represent the many-to-many relationship between items and labels.
 */
object ItemLabels : Table() {
    val item = reference("item", Items)
    val label = reference("label", Labels)
}
