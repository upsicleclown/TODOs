package todo.database.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import todo.database.tables.BooleanOperators

/**
 *  Entity instance or row created in the Priorities table.
 */
class BooleanOperator(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, BooleanOperator>(BooleanOperators)
}
