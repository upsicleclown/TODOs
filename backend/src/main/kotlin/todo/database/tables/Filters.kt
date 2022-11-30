package todo.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import todo.database.tables.Items.nullable

object Filters : IntIdTable() {
    val edtStartDateRange = datetime(name = "edtStartDateRange").nullable()
    val edtEndDateRange = datetime(name = "edtEndDateRange").nullable()
    val isCompleted = bool("is_completed").nullable()
    val labelBooleanOperator = reference("booleanOperator", BooleanOperators)
}
