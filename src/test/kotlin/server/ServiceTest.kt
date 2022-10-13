package server

import models.Group
import models.Item
import models.Label
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class ServiceTest {
    private val service = Service()

    @Test
    fun testInit() {
        // Loads items and groups on init, ensure everything is loaded.
        val expectedItemIds = listOf(1, 2, 3)
        val expectedLabelIds = listOf(1, 2, 3)
        val expectedGroupIds = listOf(1, 2, 3)

        assertEquals(service.getItems().map { item: Item -> item.id }, expectedItemIds)
        assertEquals(service.getLabels().map { label: Label -> label.id }, expectedLabelIds)
        assertEquals(service.getGroups().map { group: Group -> group.id }, expectedGroupIds)
    }

    @Test
    fun testAddItemWithNonExistingLabel() {
        val nonExistingLabelIds = mutableListOf(10000)

        assertFailsWith<IllegalArgumentException> {
            service.addItem(Item("test", false, nonExistingLabelIds))
        }
    }

    @Test
    fun testEditItemWithNonExistingLabel() {
        val existingItemId = 1
        val nonExistingLabelIds = mutableListOf(10000)

        assertFailsWith<IllegalArgumentException> {
            service.editItem(existingItemId, Item("test", false, nonExistingLabelIds))
        }
    }

    @Test
    fun testRemoveLabelFromItemsAndGroups() {
        val expectedLabelId = 1
        val itemIdWithLabel = 3
        val groupIdWithLabel = 3

        service.removeLabel(expectedLabelId)
        assert(service.getItems().first { item: Item -> item.id == itemIdWithLabel }.labelIds.isEmpty())
        assert(service.getGroups().first { group: Group -> group.id == groupIdWithLabel }.labelIds.isEmpty())
        assertFalse(service.getLabels().map { label: Label -> label.id }.contains(expectedLabelId))
    }

    @Test
    fun testAddGroupWithNonExistingLabel() {
        val nonExistingLabelIds = mutableListOf(10000)

        assertFailsWith<IllegalArgumentException> {
            service.addGroup(Group("test", nonExistingLabelIds))
        }
    }

    @Test
    fun testEditGroupWithNonExistingLabel() {
        val existingGroupId = 1
        val nonExistingLabelIds = mutableListOf(10000)

        assertFailsWith<IllegalArgumentException> {
            service.editGroup(existingGroupId, Group("test", nonExistingLabelIds))
        }
    }

}