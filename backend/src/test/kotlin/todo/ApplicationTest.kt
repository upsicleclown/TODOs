package todo

import org.json.JSONArray
import org.json.JSONTokener
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import todo.database.config.SQLiteDBConfig
import kotlin.test.assertEquals

/**
 *
 * This class holds integration tests for Application
 *
 * */
@RunWith(SpringRunner::class)
@SpringBootTest(
    classes = [Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ApplicationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

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

    @Test
    fun testFailedLogin() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val loginResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/user", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.NOT_FOUND, loginResponse.statusCode)
    }

    @Test
    fun testNotAuthenticated() {
        val getGroupsResponse = testRestTemplate.getForEntity("/groups", String::class.java)
        assertEquals(HttpStatus.FORBIDDEN, getGroupsResponse?.statusCode)
    }

    @Test
    fun testCRDUser() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        // Create new user for this test
        val newUserResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/register", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.OK, newUserResponse.statusCode)

        // Login user
        val loginResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/user", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.OK, loginResponse.statusCode)

        // Delete user
        testRestTemplate.delete("/user/test")
    }

    @Test
    fun testCRUDGroup() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        // Create new user for this test
        val newUserResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/register", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.OK, newUserResponse.statusCode)

        // Login user
        val loginResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/user", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.OK, loginResponse.statusCode)

        // Test creating new Group
        val newGroupResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/groups", HttpEntity("{\"name\":\"group4\", \"filter\":{\"labelBooleanOperator\":\"AND\"}}", headers))
        assertEquals(HttpStatus.OK, newGroupResponse.statusCode)

        // Test retrieving existing Groups
        var getGroupsResponse = testRestTemplate.getForEntity("/groups", String::class.java)
        assertEquals(HttpStatus.OK, getGroupsResponse?.statusCode)
        var getGroupsResponseBody = JSONTokener(getGroupsResponse?.body).nextValue() as JSONArray
        val newGroups = (0 until getGroupsResponseBody.length())
            .filter { i -> getGroupsResponseBody.getJSONObject(i).get("name") == "group4" }
            .map { i -> getGroupsResponseBody.getJSONObject(i) }
        assertEquals(1, newGroups.size)
        val newGroup = newGroups[0]

        // Test updating previously created Group
        testRestTemplate.put("/groups/" + newGroup.get("id"), HttpEntity("{\"name\":\"group4.1\", \"filter\":{\"edtStartDateRange\":\"2010-06-01T22:19:44\", \"edtEndDateRange\":\"2019-06-01T22:19:44\", \"isCompleted\":false, \"priorities\":[\"HIGH\", \"MEDIUM\"], \"labelBooleanOperator\":\"OR\"} }", headers))

        getGroupsResponse = testRestTemplate.getForEntity("/groups", String::class.java)
        assertEquals(HttpStatus.OK, getGroupsResponse?.statusCode)
        getGroupsResponseBody = JSONTokener(getGroupsResponse?.body).nextValue() as JSONArray
        val editedGroups = (0 until getGroupsResponseBody.length())
            .filter { i -> getGroupsResponseBody.getJSONObject(i).get("name") == "group4.1" }
            .map { i -> getGroupsResponseBody.getJSONObject(i) }
        assertEquals(1, editedGroups.size)

        // Test deleting previously created Group
        testRestTemplate.delete("/groups/" + newGroup.get("id"))

        getGroupsResponse = testRestTemplate.getForEntity("/groups", String::class.java)
        assertEquals(HttpStatus.OK, getGroupsResponse?.statusCode)
        getGroupsResponseBody = JSONTokener(getGroupsResponse?.body).nextValue() as JSONArray
        val deletedGroups = (0 until getGroupsResponseBody.length())
            .filter { i -> getGroupsResponseBody.getJSONObject(i).get("name") == "group4.1" }
            .map { i -> getGroupsResponseBody.getJSONObject(i) }
        assertEquals(0, deletedGroups.size)

        // Delete user
        testRestTemplate.delete("/user/test")
    }

    @Test
    fun testCRUDItem() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        // Create new user for this test
        val newUserResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/register", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.OK, newUserResponse.statusCode)

        // Login user
        val loginResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/user", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.OK, loginResponse.statusCode)

        // Test creating new Item
        val newItemResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/items", HttpEntity("{\"title\":\"item4\", \"isCompleted\": false}", headers))
        assertEquals(HttpStatus.OK, newItemResponse.statusCode)

        // Test retrieving existing Items
        var getItemsResponse = testRestTemplate.getForEntity("/items", String::class.java)
        assertEquals(HttpStatus.OK, getItemsResponse?.statusCode)
        var getItemsResponseBody = JSONTokener(getItemsResponse?.body).nextValue() as JSONArray
        val newItems = (0 until getItemsResponseBody.length())
            .filter { i -> getItemsResponseBody.getJSONObject(i).get("title") == "item4" }
            .map { i -> getItemsResponseBody.getJSONObject(i) }
        assertEquals(1, newItems.size)
        val newItem = newItems[0]

        // Test updating previously created Item
        testRestTemplate.put("/items/" + newItem.get("id"), HttpEntity("{\"title\":\"item4.1\", \"isCompleted\": true, \"edtDueDate\":\"2010-06-01T22:19:44\", \"priority\":\"LOW\"}", headers))

        getItemsResponse = testRestTemplate.getForEntity("/items", String::class.java)
        assertEquals(HttpStatus.OK, getItemsResponse?.statusCode)
        getItemsResponseBody = JSONTokener(getItemsResponse?.body).nextValue() as JSONArray
        val editedItems = (0 until getItemsResponseBody.length())
            .filter { i -> getItemsResponseBody.getJSONObject(i).get("title") == "item4.1" }
            .map { i -> getItemsResponseBody.getJSONObject(i) }
        assertEquals(1, editedItems.size)

        // Test deleting previously created Item
        testRestTemplate.delete("/items/" + newItem.get("id"))

        getItemsResponse = testRestTemplate.getForEntity("/items", String::class.java)
        assertEquals(HttpStatus.OK, getItemsResponse?.statusCode)
        getItemsResponseBody = JSONTokener(getItemsResponse?.body).nextValue() as JSONArray
        val deletedItems = (0 until getItemsResponseBody.length())
            .filter { i -> getItemsResponseBody.getJSONObject(i).get("title") == "item4.1" }
            .map { i -> getItemsResponseBody.getJSONObject(i) }
        assertEquals(0, deletedItems.size)

        // Delete user
        testRestTemplate.delete("/user/test")
    }

    @Test
    fun testCRUDLabel() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        // Create new user for this test
        val newUserResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/register", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.OK, newUserResponse.statusCode)

        // Login user
        val loginResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/user", HttpEntity("{\"username\":\"test\",\"password\":\"test\"}", headers))
        assertEquals(HttpStatus.OK, loginResponse.statusCode)

        // Test creating new Label
        val newLabelResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/labels", HttpEntity("{\"name\":\"label4\", \"color\":\"#FFC0CB\"}", headers))
        assertEquals(HttpStatus.OK, newLabelResponse.statusCode)

        // Test retrieving existing Labels
        var getLabelsResponse = testRestTemplate.getForEntity("/labels", String::class.java)
        assertEquals(HttpStatus.OK, getLabelsResponse?.statusCode)
        var getLabelsResponseBody = JSONTokener(getLabelsResponse?.body).nextValue() as JSONArray
        val newLabels = (0 until getLabelsResponseBody.length())
            .filter { i -> getLabelsResponseBody.getJSONObject(i).get("name") == "label4" }
            .map { i -> getLabelsResponseBody.getJSONObject(i) }
        assertEquals(1, newLabels.size)
        val newLabel = newLabels[0]

        // Test updating previously created Label
        testRestTemplate.put("/labels/" + newLabel.get("id"), HttpEntity("{\"name\":\"label4.1\", \"color\":\"#FFC0CB\"}", headers))

        getLabelsResponse = testRestTemplate.getForEntity("/labels", String::class.java)
        assertEquals(HttpStatus.OK, getLabelsResponse?.statusCode)
        getLabelsResponseBody = JSONTokener(getLabelsResponse?.body).nextValue() as JSONArray
        val editedLabels = (0 until getLabelsResponseBody.length())
            .filter { i -> getLabelsResponseBody.getJSONObject(i).get("name") == "label4.1" }
            .map { i -> getLabelsResponseBody.getJSONObject(i) }
        assertEquals(1, editedLabels.size)

        // Test deleting previously created Label
        testRestTemplate.delete("/labels/" + newLabel.get("id"))

        getLabelsResponse = testRestTemplate.getForEntity("/labels", String::class.java)
        assertEquals(HttpStatus.OK, getLabelsResponse?.statusCode)
        getLabelsResponseBody = JSONTokener(getLabelsResponse?.body).nextValue() as JSONArray
        val deletedLabels = (0 until getLabelsResponseBody.length())
            .filter { i -> getLabelsResponseBody.getJSONObject(i).get("name") == "label4.1" }
            .map { i -> getLabelsResponseBody.getJSONObject(i) }
        assertEquals(0, deletedLabels.size)

        // Delete user
        testRestTemplate.delete("/user/test")
    }
}
