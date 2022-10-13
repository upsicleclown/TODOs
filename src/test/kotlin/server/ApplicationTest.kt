package server


import junit.framework.Assert.assertEquals
import org.json.JSONArray
import org.json.JSONTokener
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class ApplicationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun testCRUDGroup() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        
        val newGroupResponse: ResponseEntity<Any> = testRestTemplate.postForEntity("/groups", HttpEntity("{\"name\":\"group4\"}", headers))
        assertEquals( HttpStatus.OK, newGroupResponse?.statusCode)

        var getGroupsResponse = testRestTemplate.getForEntity("/groups", String::class.java)
        assertEquals( HttpStatus.OK, getGroupsResponse?.statusCode)
        var getGroupsResponseBody = JSONTokener(getGroupsResponse?.body).nextValue() as JSONArray
        val newGroups = (0 until getGroupsResponseBody.length()).filter { i -> getGroupsResponseBody.getJSONObject(i).get("name") == "group4"}.map{i -> getGroupsResponseBody.getJSONObject(i)}
        assertEquals(1, newGroups.size)
        val newGroup = newGroups.get(0)

        testRestTemplate.put("/groups/"+newGroup.get("id"),  HttpEntity("{\"name\":\"group4.1\"}", headers))

        getGroupsResponse = testRestTemplate.getForEntity("/groups", String::class.java)
        assertEquals( HttpStatus.OK, getGroupsResponse?.statusCode)
        getGroupsResponseBody = JSONTokener(getGroupsResponse?.body).nextValue() as JSONArray
        val editedGroups = (0 until getGroupsResponseBody.length()).filter { i -> getGroupsResponseBody.getJSONObject(i).get("name") == "group4.1"}.map{i -> getGroupsResponseBody.getJSONObject(i)}
        assertEquals(1, editedGroups.size)

        testRestTemplate.delete("/groups/"+newGroup.get("id"))

        getGroupsResponse = testRestTemplate.getForEntity("/groups", String::class.java)
        assertEquals( HttpStatus.OK, getGroupsResponse?.statusCode)
        getGroupsResponseBody = JSONTokener(getGroupsResponse?.body).nextValue() as JSONArray
        val deletedGroups = (0 until getGroupsResponseBody.length()).filter { i -> getGroupsResponseBody.getJSONObject(i).get("name") == "group4.1"}.map{i -> getGroupsResponseBody.getJSONObject(i)}
        assertEquals(0, deletedGroups.size)
    }

    @Test
    fun testCRUDItem(){
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val newItemResponse: ResponseEntity<Any> = testRestTemplate.postForEntity("/items", HttpEntity("{\"title\":\"item4\", \"isCompleted\": false}", headers))
        assertEquals(HttpStatus.OK, newItemResponse?.statusCode)

        var getItemsResponse = testRestTemplate.getForEntity("/items", String::class.java)
        assertEquals(HttpStatus.OK, getItemsResponse?.statusCode)
        var getItemsResponseBody = JSONTokener(getItemsResponse?.body).nextValue() as JSONArray
        val newItems = (0 until getItemsResponseBody.length()).filter { i -> getItemsResponseBody.getJSONObject(i).get("title") == "item4"}.map{i -> getItemsResponseBody.getJSONObject(i)}
        assertEquals(1, newItems.size)
        val newItem = newItems.get(0)

        testRestTemplate.put("/items/"+newItem.get("id"),  HttpEntity("{\"title\":\"item4.1\", \"isCompleted\": false}", headers))

        getItemsResponse = testRestTemplate.getForEntity("/items", String::class.java)
        assertEquals( HttpStatus.OK, getItemsResponse?.statusCode)
        getItemsResponseBody = JSONTokener(getItemsResponse?.body).nextValue() as JSONArray
        val editedItems = (0 until getItemsResponseBody.length()).filter { i -> getItemsResponseBody.getJSONObject(i).get("title") == "item4.1"}.map{i -> getItemsResponseBody.getJSONObject(i)}
        assertEquals(1, editedItems.size)

        testRestTemplate.delete("/items/"+newItem.get("id"))

        getItemsResponse = testRestTemplate.getForEntity("/items", String::class.java)
        assertEquals(HttpStatus.OK, getItemsResponse?.statusCode)
        getItemsResponseBody = JSONTokener(getItemsResponse?.body).nextValue() as JSONArray
        val deletedItems = (0 until getItemsResponseBody.length()).filter { i -> getItemsResponseBody.getJSONObject(i).get("title") == "item4.1"}.map{i -> getItemsResponseBody.getJSONObject(i)}
        assertEquals(0, deletedItems.size)
    }

    @Test
    fun testCRDLabels(){
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val newLabelResponse: ResponseEntity<Any> = testRestTemplate.postForEntity("/labels", HttpEntity("{\"name\":\"label4\"}", headers))
        assertEquals( HttpStatus.OK, newLabelResponse?.statusCode)

        var getLabelsResponse = testRestTemplate.getForEntity("/labels", String::class.java)
        assertEquals( HttpStatus.OK, getLabelsResponse?.statusCode)
        var getLabelsResponseBody = JSONTokener(getLabelsResponse?.body).nextValue() as JSONArray
        val newLabels = (0 until getLabelsResponseBody.length()).filter { i -> getLabelsResponseBody.getJSONObject(i).get("name") == "label4"}.map{i -> getLabelsResponseBody.getJSONObject(i)}
        assertEquals(1, newLabels.size)
        val newLabel = newLabels.get(0)

        testRestTemplate.delete("/labels/"+newLabel.get("id"))

        getLabelsResponse = testRestTemplate.getForEntity("/labels", String::class.java)
        assertEquals( HttpStatus.OK, getLabelsResponse?.statusCode)
        getLabelsResponseBody = JSONTokener(getLabelsResponse?.body).nextValue() as JSONArray
        val deletedLabels = (0 until getLabelsResponseBody.length()).filter { i -> getLabelsResponseBody.getJSONObject(i).get("name") == "label4.1"}.map{i -> getLabelsResponseBody.getJSONObject(i)}
        assertEquals(0, deletedLabels.size)
    }
}