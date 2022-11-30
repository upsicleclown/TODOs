package todo.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import todo.database.tables.FilterLabels
import todo.database.tables.FilterPriorities
import todo.database.tables.Filters

/**
 *  Entity instance or row created in the Filters table.
 */
class Filter(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Filter>(Filters)
    var edtStartDateRange by Filters.edtStartDateRange
    var edtEndDateRange by Filters.edtEndDateRange
    var isComplete by Filters.isCompleted
    var priorities by Priority via FilterPriorities
    var labelBooleanOperator by Filters.labelBooleanOperator
    var labels by Label via FilterLabels
}
