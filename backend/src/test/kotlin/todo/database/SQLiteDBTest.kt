package todo.database

import models.Group
import models.Item
import models.Label
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class SQLiteDBTest {
    private val db = MockSQLiteDB()

    /**
     * Before each test we lock the mock data.
     */
    @BeforeTest
    fun setup() {
        db.loadData()
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

        assertEquals(db.getItems().map { item: Item -> item.id }, expectedItemIds)
        assertEquals(db.getLabels().map { label: Label -> label.id }, expectedLabelIds)
        assertEquals(db.getGroups().map { group: Group -> group.id }, expectedGroupIds)
    }

    /**
     *  Tests related to items.
     */
    @Test
    fun testAddItem() {
        val expectedItemId = 1
        val expectedItemTitle = "item4"
        val expectedItemIsCompleted = true

        db.addItem(Item(expectedItemTitle, expectedItemIsCompleted))
        assert(db.getItems().contains(Item(expectedItemTitle, expectedItemIsCompleted, id = expectedItemId)))
    }

    @Test
    fun testRemoveItem() {
        val expectedItemId = 1

        db.removeItem(expectedItemId)
        assertFalse(db.getItems().map { item: Item -> item.id }.contains(expectedItemId))
    }

    @Test
    fun testEditItem() {
        val expectedItemId = 1
        val newTitle = "newTest1"

        db.editItem(expectedItemId, Item(newTitle, false))
        assert(db.getItems().first { item: Item -> item.id == expectedItemId }.title == newTitle)
    }

    @Test
    fun testGetItems() {
        val expectedItemIds = listOf(1, 2, 3, 4)

        db.addItem(Item("test4", false))
        assertEquals(db.getItems().map { item: Item -> item.id }, expectedItemIds)
    }

    @Test
    fun testLoadItems() {
        val expectedItemIds = listOf(1, 2, 3)

        // load is already done in @BeforeTest
        assertEquals(db.getItems().map { item: Item -> item.id }, expectedItemIds)
    }

    /**
     * Tests related to labels.
     */
    @Test
    fun testAddLabel() {
        val expectedLabelId = 4
        val expectedLabelName = "label4"

        db.addLabel(Label(expectedLabelName))
        assert(db.getLabels().contains(Label(expectedLabelName, id = expectedLabelId)))
    }

    @Test
    fun testRemoveLabel() {
        val expectedLabelId = 1

        db.removeLabel(expectedLabelId)
        assertFalse(db.getLabels().map { label: Label -> label.id }.contains(expectedLabelId))
    }

    @Test
    fun testEditLabel() {
        val expectedLabelId = 1
        val newName = "newLabel1"

        db.editLabel(expectedLabelId, Label(newName))
        assert(db.getLabels().first { label: Label -> label.id == expectedLabelId }.name == newName)
    }

    @Test
    fun testGetLabels() {
        val expectedLabelIds = listOf(1, 2, 3, 4)

        db.addLabel(Label("test4"))
        assertEquals(db.getLabels().map { label: Label -> label.id }, expectedLabelIds)
    }

    @Test
    fun testLoadLabels() {
        val expectedLabelIds = listOf(1, 2, 3)

        // load is already done in @BeforeTest
        assertEquals(db.getLabels().map { label: Label -> label.id }, expectedLabelIds)
    }

    /**
     * Tests related to groups.
     */
    @Test
    fun testAddGroup() {
        val expectedGroupId = 4
        val expectedGroupTitle = "group4"

        db.addGroup(Group(expectedGroupTitle))
        assert(db.getGroups().contains(Group(expectedGroupTitle, id = expectedGroupId)))
    }

    @Test
    fun testRemoveGroup() {
        val expectedGroupId = 1

        db.removeGroup(expectedGroupId)
        assertFalse(db.getGroups().map { group: Group -> group.id }.contains(expectedGroupId))
    }

    @Test
    fun testEditGroup() {
        val expectedGroupId = 1
        val newName = "newGroup1"

        db.editGroup(expectedGroupId, Group(newName))
        assert(db.getGroups().first { group: Group -> group.id == expectedGroupId }.name == newName)
    }

    @Test
    fun testGetGroups() {
        val expectedGroupIds = listOf(1, 2, 3, 4)

        db.addGroup(Group("group4"))
        assertEquals(db.getGroups().map { group: Group -> group.id }, expectedGroupIds)
    }

    @Test
    fun testLoadGroups() {
        val expectedGroupIds = listOf(1, 2, 3)

        // load is already done in @BeforeTest
        assertEquals(db.getGroups().map { group: Group -> group.id }, expectedGroupIds)
    }

    /**
     * Tests related to items, groups and labels.
     */
    @Test
    fun testRemoveLabelFromItemsAndGroups() {
        val expectedLabelId = 1
        val itemIdWithLabel = 3
        val groupIdWithLabel = 3

        db.removeLabel(expectedLabelId)
        assert(db.getItems().first { item: Item -> item.id == itemIdWithLabel }.labelIds.isEmpty())
        assert(db.getGroups().first { group: Group -> group.id == groupIdWithLabel }.labelIds.isEmpty())
        assertFalse(db.getLabels().map { label: Label -> label.id }.contains(expectedLabelId))
    }
}