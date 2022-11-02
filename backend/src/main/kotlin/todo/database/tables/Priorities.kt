package todo.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Priorities : IdTable<String>() {
    val name = varchar("name", 25).uniqueIndex()
    override val id: Column<EntityID<String>> = name.entityId()
}
