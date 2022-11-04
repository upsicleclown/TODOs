package todo.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import todo.database.tables.ItemLabels
import todo.database.tables.Items

/**
 *  Entity instance or row created in the Items table.
 */
class Item(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Item>(Items)
    var title by Items.title
    var isComplete by Items.isCompleted
    var labels by Label via ItemLabels
    var edtDueDate by Items.edtDueDate
    var priority by Items.priority
    var user by User referencedOn Items.user
}
