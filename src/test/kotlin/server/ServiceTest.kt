package server

import models.Group
import models.Item
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ServiceTest {
    private val service = Service(
        this.javaClass.classLoader.getResource("server/database/items.json")!!.path,
        this.javaClass.classLoader.getResource("server/database/groups.json")!!.path
    )

    @Test
    fun testInit() {
        // Loads items and groups on init, ensure everything is loaded.
        val expectedItemIds = listOf(1, 2, 3)
        val expectedGroupIds = listOf(1, 2, 3)

        assertEquals(service.getItems().map { item: Item -> item.id }, expectedItemIds)
        assertEquals(service.getGroups().map { group: Group -> group.id }, expectedGroupIds)
    }

    @Test
    fun testAddItemToNonExistingGroup() {
        val nonExistingGroupId = 1000000

        assertFailsWith<IllegalArgumentException> {
            service.addItem(Item("test", false, nonExistingGroupId))
        }
    }

    @Test
    fun testEditItemToNonExistingGroup() {
        val existingItemId = 1
        val nonExistingGroupId = 1000000

        assertFailsWith<IllegalArgumentException> {
            service.editItem(existingItemId, Item("test", false, nonExistingGroupId))
        }
    }

    @Test
    fun testRemoveGroupWithItems() {
        val expectedGroupId = 1
        val itemIdInGroup = 3

        service.removeGroup(expectedGroupId)
        assertNull(service.getItems().first { item: Item -> item.id == itemIdInGroup }.groupId)
        assertFalse(service.getGroups().map { group: Group -> group.id }.contains(expectedGroupId))
    }

}