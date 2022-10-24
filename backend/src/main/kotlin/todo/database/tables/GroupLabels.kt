package todo.database.tables

import org.jetbrains.exposed.sql.Table

/**
 *  Intermediate database table to represent the many-to-many relationship between groups and labels.
 */
object GroupLabels : Table() {
    val group = reference("group", Groups)
    val label = reference("label", Labels)
}
