package todo.service

import models.BooleanOperator
import models.Filter
import models.Group
import models.Item
import models.User
import org.junit.jupiter.api.BeforeAll
import todo.database.config.SQLiteDBConfig
import todo.service.authentication.AuthenticationService
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
    private val authenticationService = AuthenticationService()
    private val service = Service()

    /**
     * Before all tests, initialize db configs.
     */
    companion object {
        @JvmStatic
        @BeforeAll
        fun initialize() {
            SQLiteDBConfig().initialize()
        }
    }

    /**
     * Before each test we authenticate a test user.
     */
    @BeforeTest
    fun setup() {
        val testUser = User("test", "test")
        authenticationService.registerUser(testUser)
        authenticationService.logInUser(testUser)
    }

    /**
     * To make each test idempotent, after each test, we un-authenticate the test user.
     */
    @AfterTest
    fun clean() {
        authenticationService.removeUser("test")
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
            service.addGroup(Group("test", Filter(labelIds = nonExistingLabelIds, labelBooleanOperator = BooleanOperator.AND)))
        }
    }

    @Test
    fun testEditGroupWithNonExistingLabel() {
        val existingGroupId = 1
        val nonExistingLabelIds = mutableListOf(10000)

        assertFailsWith<IllegalArgumentException> {
            service.editGroup(existingGroupId, Group("test", Filter(labelIds = nonExistingLabelIds, labelBooleanOperator = BooleanOperator.AND)))
        }
    }
}
