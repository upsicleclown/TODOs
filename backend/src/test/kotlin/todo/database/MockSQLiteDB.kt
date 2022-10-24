package todo.database

import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction
import todo.database.models.Group
import todo.database.models.Item
import todo.database.models.Label

/**
 * Mock class of `SQLiteDB` to pre-populate the database with sample data so the class functionalities can be tested.
 */
class MockSQLiteDB : SQLiteDB("jdbc:sqlite:src/test/kotlin/todo/database/test.db") {
    /**
     * Load database with mock data.
     */
    fun loadData() {
        transaction {
            // Label data
            val label1 = Label.new {
                name = "label1"
            }
            Label.new {
                name = "label2"
            }
            Label.new {
                name = "label3"
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
            }
            Group.new {
                name = "group2"
            }
            Group.new {
                name = "group3"
                labels = SizedCollection(listOf(label1))
            }
        }
    }
}
