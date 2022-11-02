package todo.database.tables

import models.Priority
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.compoundOr
import org.jetbrains.exposed.sql.javatime.datetime

/**
 *  Items database table.
 */
object Items : IntIdTable() {
    val title = varchar("title", 50)
    val isCompleted = bool("is_completed")
    val edtDueDate = datetime(name = "edtDueDate").nullable()
    val priority = varchar("priority", 25).check { (Priority.values().map { priorityEnum -> Op.build { it eq priorityEnum.name } } + it.isNull()).compoundOr() }.nullable()
}
