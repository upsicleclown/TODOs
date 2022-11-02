package todo.database

import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import todo.database.models.Filter
import todo.database.models.Group
import todo.database.models.Item
import todo.database.models.Label
import todo.database.tables.Labels
import todo.database.tables.Priorities
import java.time.LocalDateTime

/**
 * Mock class of `SQLiteDB` to pre-populate the database with sample data so the class functionalities can be tested.
 */
class MockSQLiteDB : SQLiteDB("jdbc:sqlite:src/test/kotlin/todo/database/test.db") {
    val TEST_LABEL_COLOR = "#000000"

    /**
     * Load database with mock data.
     */
    fun loadData() {
        transaction {
            // Label data
            val label1 = Label.new {
                name = "label1"
                color = TEST_LABEL_COLOR
            }
            Label.new {
                name = "label2"
                color = TEST_LABEL_COLOR
            }
            Label.new {
                name = "label3"
                color = TEST_LABEL_COLOR
            }

            // Item data
            Item.new {
                title = "item1"
                isComplete = false
            }
            Item.new {
                title = "item2"
                isComplete = false
            }
            Item.new {
                title = "item3"
                isComplete = true
                labels = SizedCollection(listOf(label1))
            }

            // Group data
            Group.new {
                name = "group1"
                filter = Filter.new {}
            }
            Group.new {
                name = "group2"
                filter = Filter.new {
                    edtStartDateRange = LocalDateTime.parse("2010-06-01T22:19:44")
                    edtEndDateRange = LocalDateTime.parse("2016-06-01T22:19:44")
                    isComplete = true
                }
            }
            Group.new {
                name = "group3"
                filter = Filter.new {
                    edtStartDateRange = LocalDateTime.parse("2010-06-01T22:19:44")
                    edtEndDateRange = LocalDateTime.parse("2016-06-01T22:19:44")
                    isComplete = false
                    priorities = todo.database.models.Priority.find {
                        (Priorities.id eq "LOW").or(Priorities.id eq "MEDIUM")
                    }
                    labels = Label.find {
                        Labels.name eq "label1"
                    }
                }
            }
        }
    }
}
