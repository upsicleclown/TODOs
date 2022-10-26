package todo.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 *  Items database table.
 */
object Items : IntIdTable() {
    val title = varchar("title", 50)
    val isCompleted = bool("is_completed")
}
