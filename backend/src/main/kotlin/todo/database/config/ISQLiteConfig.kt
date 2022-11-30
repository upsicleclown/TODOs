package todo.database.config

import models.BooleanOperator
import models.Priority
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import todo.database.tables.BooleanOperators
import todo.database.tables.FilterLabels
import todo.database.tables.FilterPriorities
import todo.database.tables.Filters
import todo.database.tables.Groups
import todo.database.tables.ItemLabels
import todo.database.tables.Items
import todo.database.tables.Labels
import todo.database.tables.Priorities
import todo.database.tables.Users

/**
 * Interface for SQLite configurations.
 */
interface ISQLiteConfig {
    val connectionString: String

    /**
     * Default implementation.
     *
     * Creates db tables if they do not exist and populates with required data.
     */
    fun initialize() {
        Database.connect(connectionString)
        transaction {
            addLogger(StdOutSqlLogger)

            // create tables.
            SchemaUtils.create(Users)
            SchemaUtils.create(Items)
            SchemaUtils.create(Groups)
            SchemaUtils.create(Labels)
            SchemaUtils.create(ItemLabels)
            SchemaUtils.create(Priorities)
            SchemaUtils.create(Filters)
            SchemaUtils.create(FilterLabels)
            SchemaUtils.create(FilterPriorities)

            // populates tables when required data.
            Priority.values().forEach { enum ->
                Priorities.insertIgnore {
                    it[this.name] = enum.name
                }
            }
            BooleanOperator.values().forEach { enum ->
                BooleanOperators.insertIgnore {
                    it[this.name] = enum.name
                }
            }
        }
    }
}
