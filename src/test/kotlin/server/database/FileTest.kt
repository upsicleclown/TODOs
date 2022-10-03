package server.database

import models.Group
import models.Item
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class FileTest {
    // We know the files exist.
    private val db: FileDB = FileDB(
        this.javaClass.classLoader.getResource("server/database/items.json")!!.path,
        this.javaClass.classLoader.getResource("server/database/groups.json")!!.path
    )

    @BeforeTest
    fun setup() {
        db.loadItems()
        db.loadGroups()
    }

    /*
       Tests related to items.
     */
    @Test
    fun testAddItem() {
        val expectedItemId = 4
        val expectedItemTitle = "item4"
        val expectedItemIsCompleted = true

        db.addItem(Item(expectedItemTitle, expectedItemIsCompleted))
        assert(db.getItems().contains(Item(expectedItemTitle, expectedItemIsCompleted, id=expectedItemId)))
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

    /*
       Tests related to groups.
     */
    @Test
    fun testAddGroup() {
        val expectedGroupId = 4
        val expectedGroupTitle = "group4"

        db.addGroup(Group(expectedGroupTitle))
        assert(db.getGroups().contains(Group(expectedGroupTitle, id=expectedGroupId)))
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
}