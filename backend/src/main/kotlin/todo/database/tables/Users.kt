package todo.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 *  Users database table.
 */
object Users : IntIdTable() {
    val username = varchar("username", 256)
    val passwordHash = varchar("password_hash", 256)
}
