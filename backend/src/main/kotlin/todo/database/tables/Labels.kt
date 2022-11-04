package todo.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 *  Labels database table.
 */
object Labels : IntIdTable() {
    val name = varchar("name", 50)
    val color = char("color", 7)
    val user = reference("user", Users)
}
