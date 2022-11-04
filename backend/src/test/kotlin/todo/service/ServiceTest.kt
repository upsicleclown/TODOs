package todo.service

import models.Filter
import models.Group
import models.Item
import models.User
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

/**
 *
 * This class holds integration tests for Service
 *
 * */
class ServiceTest {
    private val service = Service()

    /**
     * Before each test we authenticate a test user.
     */
    @BeforeTest
    fun setup() {
        val testUser = User("test", "test")
        service.registerUser(testUser)
        service.logInUser(testUser)
    }

    /**
     * To make each test idempotent, after each test, we un-authenticate the test user.
     */
    @AfterTest
    fun clean() {
        service.removeUser("test")
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
