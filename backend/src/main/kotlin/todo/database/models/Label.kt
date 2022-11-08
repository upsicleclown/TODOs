package todo.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import todo.database.tables.Labels

/**
 *  Entity instance or row created in the Labels table.
 */

class Label(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Label>(Labels)
    var name by Labels.name
    var color by Labels.color
    var user by User referencedOn Labels.user
}
