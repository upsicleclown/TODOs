package todo.service

import models.Filter
import models.Group
import models.Item
import kotlin.test.Test
import kotlin.test.assertFailsWith

/**
 *
 * This class holds integration tests for Service
 *
 * */
class ServiceTest {
    private val service = Service()

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
    fun testAddGroupWithNonExistingLabel() {
        val nonExistingLabelIds = mutableListOf(10000)

        assertFailsWith<IllegalArgumentException> {
            service.addGroup(Group("test", Filter(labelIds = nonExistingLabelIds)))
        }
    }

    @Test
    fun testEditGroupWithNonExistingLabel() {
        val existingGroupId = 1
        val nonExistingLabelIds = mutableListOf(10000)

        assertFailsWith<IllegalArgumentException> {
            service.editGroup(existingGroupId, Group("test", Filter(labelIds = nonExistingLabelIds)))
        }
    }
}
