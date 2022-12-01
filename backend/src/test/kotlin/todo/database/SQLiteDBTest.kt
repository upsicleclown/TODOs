package todo.database

import models.BooleanOperator
import models.Filter
import models.Group
import models.Item
import models.Label
import models.Priority
import models.User
import java.io.File
import java.time.LocalDateTime
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class SQLiteDBTest {
    private val dbConfig = SQLiteDBTestConfig()

    /**
     * Before each test we lock the mock data.
     */
    @BeforeTest
    fun setup() {
        dbConfig.initialize()
    }

    /**
     * To make each test idempotent, after each test, we delete the database.
     */
    @AfterTest
    fun cleanup() {
        File("src/test/kotlin/todo/database/test.db").delete()
    }

    /**
     * Tests related to mock data loading.
     */
    @Test
    fun testInit() {
        // Loads items, labels and groups on init, ensure everything is loaded.
        val expectedItemIds = listOf(1, 2, 3)
        val expectedLabelIds = listOf(1, 2, 3)
        val expectedGroupIds = listOf(1, 2, 3)

        assertEquals(SQLiteDB.getItems().map { item: Item -> item.id }, expectedItemIds)
        assertEquals(SQLiteDB.getLabels().map { label: Label -> label.id }, expectedLabelIds)
        assertEquals(SQLiteDB.getGroups().map { group: Group -> group.id }, expectedGroupIds)
    }

    /**
     *  Tests related to users.
     */
    @Test
    fun testAddUser() {
        val expectedUser = User("user3", "test")
        val expectedPasswordHash = "VgEyqh5ghWaqHU0v7xOJtN0eG2zqSO59K4TbFoFf6uM="

        SQLiteDB.addUser(expectedUser, expectedPasswordHash)
        assertEquals(SQLiteDB.getUser(expectedUser, expectedPasswordHash), expectedUser)
    }

    /**
     *  Tests related to items.
     */
    @Test
    fun testAddItem() {
        val expectedItemId = 5
        val expectedItemTitle = "item4"
        val expectedItemIsCompleted = true

        val insertedItem = SQLiteDB.addItem(Item(expectedItemTitle, expectedItemIsCompleted))
        assertEquals(expectedItemId, insertedItem.id)
    }

    @Test
    fun testRemoveItem() {
        val expectedItemId = 1

        SQLiteDB.removeItem(expectedItemId)
        assertFalse(SQLiteDB.getItems().map { item: Item -> item.id }.contains(expectedItemId))
    }

    @Test
    fun testEditItem() {
        val expectedItemId = 1
        val newTitle = "newTest1"

        SQLiteDB.editItem(expectedItemId, Item(newTitle, false))
        assert(SQLiteDB.getItems().first { item: Item -> item.id == expectedItemId }.title == newTitle)
    }

    @Test
    fun testGetItems() {
        val expectedItemIds = listOf(1, 2, 3, 5)

        SQLiteDB.addItem(Item("test4", false))
        assertEquals(SQLiteDB.getItems().map { item: Item -> item.id }, expectedItemIds)
    }

    @Test
    fun testLoadItems() {
        val expectedItemIds = listOf(1, 2, 3)

        // load is already done in @BeforeTest
        assertEquals(SQLiteDB.getItems().map { item: Item -> item.id }, expectedItemIds)
    }

    /**
     * Tests related to labels.
     */
    @Test
    fun testAddLabel() {
        val expectedLabelId = 5
        val expectedLabelName = "label4"
        val expectedLabelColor = "#FFFFFF"

        val insertedLabel = SQLiteDB.addLabel(Label(expectedLabelName, expectedLabelColor))
        assertEquals(expectedLabelId, insertedLabel.id)
    }

    @Test
    fun testRemoveLabel() {
        val expectedLabelId = 1

        SQLiteDB.removeLabel(expectedLabelId)
        assertFalse(SQLiteDB.getLabels().map { label: Label -> label.id }.contains(expectedLabelId))
    }

    @Test
    fun testEditLabel() {
        val expectedLabelId = 1
        val newName = "newLabel1"
        val newLabelColor = "#000000"

        SQLiteDB.editLabel(expectedLabelId, Label(newName, newLabelColor))

        val newLabel = SQLiteDB.getLabels().first { label: Label -> label.id == expectedLabelId }

        assert(newLabel.name == newName && newLabel.color == newLabelColor)
    }

    @Test
    fun testGetLabels() {
        val expectedLabelIds = listOf(1, 2, 3, 5)

        SQLiteDB.addLabel(Label("test4", "#000000"))
        assertEquals(SQLiteDB.getLabels().map { label: Label -> label.id }, expectedLabelIds)
    }

    @Test
    fun testLoadLabels() {
        val expectedLabelIds = listOf(1, 2, 3)

        // load is already done in @BeforeTest
        assertEquals(SQLiteDB.getLabels().map { label: Label -> label.id }, expectedLabelIds)
    }

    /**
     * Tests related to groups.
     */
    @Test
    fun testAddGroup() {
        val expectedGroupId = 5
        val expectedGroupTitle = "group4"
        val expectedGroupFilter = Filter(
            LocalDateTime.parse("2010-06-01T22:19:44"),
            LocalDateTime.parse("2016-06-01T22:19:44"),
            false,
            mutableListOf(Priority.MEDIUM, Priority.LOW),
            mutableListOf(),
            BooleanOperator.AND
        )

        val insertedGroup = SQLiteDB.addGroup(Group(expectedGroupTitle, expectedGroupFilter))
        assertEquals(expectedGroupId, insertedGroup.id)
    }

    @Test
    fun testRemoveGroup() {
        val expectedGroupId = 1

        SQLiteDB.removeGroup(expectedGroupId)
        assertFalse(SQLiteDB.getGroups().map { group: Group -> group.id }.contains(expectedGroupId))
    }

    @Test
    fun testEditGroup() {
        val expectedGroupId = 1
        val newName = "newGroup1"
        val newGroupFilter = Filter(
            LocalDateTime.parse("2010-06-01T22:19:44"),
            LocalDateTime.parse("2016-06-01T22:19:44"),
            false,
            mutableListOf(Priority.MEDIUM, Priority.LOW),
            mutableListOf(),
            BooleanOperator.OR
        )

        SQLiteDB.editGroup(expectedGroupId, Group(newName, newGroupFilter))

        val editedGroup = SQLiteDB.getGroups().first { group: Group -> group.id == expectedGroupId }
        assert(editedGroup.name == newName && editedGroup.filter == newGroupFilter)
    }

    @Test
    fun testGetGroups() {
        val expectedGroupIds = listOf(1, 2, 3, 5)

        SQLiteDB.addGroup(Group("group4", Filter(labelBooleanOperator = BooleanOperator.AND)))
        assertEquals(SQLiteDB.getGroups().map { group: Group -> group.id }, expectedGroupIds)
    }

    @Test
    fun testLoadGroups() {
        val expectedGroupIds = listOf(1, 2, 3)

        // load is already done in @BeforeTest
        assertEquals(SQLiteDB.getGroups().map { group: Group -> group.id }, expectedGroupIds)
    }

    /**
     * Tests related to items, groups and labels.
     */
    @Test
    fun testRemoveLabelFromItemsAndGroups() {
        val expectedLabelId = 1
        val itemIdWithLabel = 3
        val groupIdWithLabel = 3

        SQLiteDB.removeLabel(expectedLabelId)
        assert(SQLiteDB.getItems().first { item: Item -> item.id == itemIdWithLabel }.labelIds.isEmpty())
        assert(SQLiteDB.getGroups().first { group: Group -> group.id == groupIdWithLabel }.filter.labelIds.isEmpty())
        assertFalse(SQLiteDB.getLabels().map { label: Label -> label.id }.contains(expectedLabelId))
    }
}
