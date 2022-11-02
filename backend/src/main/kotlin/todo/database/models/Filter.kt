package todo.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import todo.database.tables.FilterLabels
import todo.database.tables.FilterPriorities
import todo.database.tables.GroupFilters

/**
 *  Entity instance or row created in the GroupFilters table.
 */
class Filter(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Filter>(GroupFilters)
    var edtStartDateRange by GroupFilters.edtStartDateRange
    var edtEndDateRange by GroupFilters.edtEndDateRange
    var isComplete by GroupFilters.isCompleted
    var priorities by Priority via FilterPriorities
    var labels by Label via FilterLabels
}
