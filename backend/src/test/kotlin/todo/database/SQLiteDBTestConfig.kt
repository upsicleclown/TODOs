package todo.database

import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import todo.database.config.ISQLiteConfig
import todo.database.models.Filter
import todo.database.models.Group
import todo.database.models.Item
import todo.database.models.Label
import todo.database.models.User
import todo.database.tables.BooleanOperators
import todo.database.tables.Labels
import todo.database.tables.Priorities
import java.time.LocalDateTime

/**
 * Test configuration.
 *
 * Pre-populates the database with sample data so the `SQLiteDB` class functionalities can be tested.
 */
class SQLiteDBTestConfig(override val connectionString: String = "jdbc:sqlite:src/test/kotlin/todo/database/test.db") : ISQLiteConfig {
    private val TEST_LABEL_COLOR = "#000000"

    /**
     * Load database with mock data.
     */
    override fun initialize() {
        super.initialize()
        transaction {
            // User data
            val user1 = User.new {
                username = "user1"
                // hash for "test"
                passwordHash = "VgEyqh5ghWaqHU0v7xOJtN0eG2zqSO59K4TbFoFf6uM="
            }
            val user2 = User.new {
                username = "user2"
                // hash for "test"
                passwordHash = "VgEyqh5ghWaqHU0v7xOJtN0eG2zqSO59K4TbFoFf6uM="
            }
            SQLiteDB.setUserLoggedIn(models.User(user1.username, "test"))

            // Label data
            val label1 = Label.new {
                name = "label1"
                color = TEST_LABEL_COLOR
                user = user1
            }
            Label.new {
                name = "label2"
                color = TEST_LABEL_COLOR
                user = user1
            }
            Label.new {
                name = "label3"
                color = TEST_LABEL_COLOR
                user = user1
            }
            Label.new {
                name = "label4"
                color = TEST_LABEL_COLOR
                user = user2
            }

            // Item data
            Item.new {
                title = "item1"
                isComplete = false
                user = user1
            }
            Item.new {
                title = "item2"
                isComplete = false
                user = user1
            }
            Item.new {
                title = "item3"
                isComplete = true
                labels = SizedCollection(listOf(label1))
                user = user1
            }
            Item.new {
                title = "item4"
                isComplete = false
                user = user2
            }

            // Group data
            Group.new {
                name = "group1"
                filter = Filter.new {
                    labelBooleanOperator = BooleanOperators.select { BooleanOperators.id eq "AND" }.single()[BooleanOperators.id]
                }
                user = user1
            }
            Group.new {
                name = "group2"
                filter = Filter.new {
                    edtStartDateRange = LocalDateTime.parse("2010-06-01T22:19:44")
                    edtEndDateRange = LocalDateTime.parse("2016-06-01T22:19:44")
                    labelBooleanOperator = BooleanOperators.select { BooleanOperators.id eq "AND" }.single()[BooleanOperators.id]
                    isComplete = true
                }
                user = user1
            }
            Group.new {
                name = "group3"
                filter = Filter.new {
                    edtStartDateRange = LocalDateTime.parse("2010-06-01T22:19:44")
                    edtEndDateRange = LocalDateTime.parse("2016-06-01T22:19:44")
                    isComplete = false
                    labelBooleanOperator = BooleanOperators.select { BooleanOperators.id eq "AND" }.single()[BooleanOperators.id]
                    priorities = todo.database.models.Priority.find {
                        (Priorities.id eq "LOW").or(Priorities.id eq "MEDIUM")
                    }
                    labels = Label.find {
                        Labels.name eq "label1"
                    }
                }
                user = user1
            }
            Group.new {
                name = "group4"
                filter = Filter.new {
                    labelBooleanOperator = BooleanOperators.select { BooleanOperators.id eq "OR" }.single()[BooleanOperators.id]
                }
                user = user2
            }
        }
    }
}
