package server

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
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

    @Test
    fun testRUWindowSettings() {
        val expectedValue = 1000.0
        val newHeight = 600.0
        val newWidth = 400.0

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        // Test retrieving existing WindowSettings
        var getWindowSettingsResponse = testRestTemplate.getForEntity("/window-settings", String::class.java)
        assertEquals(HttpStatus.OK, getWindowSettingsResponse?.statusCode)
        var getWindowSettingsResponseBody = JSONTokener(getWindowSettingsResponse?.body).nextValue() as JSONObject
        assertEquals(getWindowSettingsResponseBody["height"] as Double, expectedValue)
        assertEquals(getWindowSettingsResponseBody["width"] as Double, expectedValue)

        // Test updating existing WindowSettings
        testRestTemplate.put("/window-settings", HttpEntity("{\"height\":$newHeight, \"width\": $newWidth}", headers))

        getWindowSettingsResponse = testRestTemplate.getForEntity("/window-settings", String::class.java)
        assertEquals(HttpStatus.OK, getWindowSettingsResponse?.statusCode)
        getWindowSettingsResponseBody = JSONTokener(getWindowSettingsResponse?.body).nextValue() as JSONObject
        assertEquals(getWindowSettingsResponseBody["height"] as Double, newHeight)
        assertEquals(getWindowSettingsResponseBody["width"] as Double, newWidth)
    }

    @Test
    fun testCRUDGroup() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        // Test creating new Group
        val newGroupResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/groups", HttpEntity("{\"name\":\"group4\"}", headers))
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
        testRestTemplate.put("/groups/" + newGroup.get("id"), HttpEntity("{\"name\":\"group4.1\"}", headers))

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
    }

    @Test
    fun testCRUDItem() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

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
        testRestTemplate.put("/items/" + newItem.get("id"), HttpEntity("{\"title\":\"item4.1\", \"isCompleted\": false}", headers))

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
    }

    @Test
    fun testCRDLabels() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        // Test creating new Label
        val newLabelResponse: ResponseEntity<Any> = testRestTemplate
            .postForEntity("/labels", HttpEntity("{\"name\":\"label4\"}", headers))
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

        // Test deleting previously created Label
        testRestTemplate.delete("/labels/" + newLabel.get("id"))

        getLabelsResponse = testRestTemplate.getForEntity("/labels", String::class.java)
        assertEquals(HttpStatus.OK, getLabelsResponse?.statusCode)
        getLabelsResponseBody = JSONTokener(getLabelsResponse?.body).nextValue() as JSONArray
        val deletedLabels = (0 until getLabelsResponseBody.length())
            .filter { i -> getLabelsResponseBody.getJSONObject(i).get("name") == "label4.1" }
            .map { i -> getLabelsResponseBody.getJSONObject(i) }
        assertEquals(0, deletedLabels.size)
    }
}
