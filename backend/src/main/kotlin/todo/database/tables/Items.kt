package todo.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

/**
 *  Items database table.
 */
object Items : IntIdTable() {
    val title = varchar("title", 50)
    val isCompleted = bool("is_completed")
    val priority = reference("priority", Priorities).nullable()
    val edtDueDate = datetime(name = "edtDueDate").nullable()
    val user = reference("user", Users)
}
