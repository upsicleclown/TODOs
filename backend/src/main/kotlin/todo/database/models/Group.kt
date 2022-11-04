package todo.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import todo.database.tables.Groups

/**
 *  Entity instance or row created in the Groups table.
 */
class Group(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Group>(Groups)
    var name by Groups.name
    var filter by Filter referencedOn Groups.filter
    var user by User referencedOn Groups.user
}
