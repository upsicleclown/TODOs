package todo.database.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import todo.database.tables.Priorities

/**
 *  Entity instance or row created in the Priorities table.
 */
class Priority(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, Priority>(Priorities)
}
