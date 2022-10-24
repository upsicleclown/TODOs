package todo.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 *  Groups database table.
 */
object Groups : IntIdTable() {
    val name = varchar("name", 50)
}
