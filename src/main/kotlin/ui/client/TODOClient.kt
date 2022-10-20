package ui.client

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import models.Group
import models.Item
import java.net.URL

class TODOClient {

    // TODO: This will change once we deploy the service to the cloud, this could also be a secret.
    private val serviceEndpoint = "http://localhost:8080/"

    /* Methods related to item endpoint */
    fun getItems(): List<Item> {
        val itemsResponse = URL("${serviceEndpoint}items").readText()
        return Json.decodeFromString(itemsResponse)
    }

    /* Methods related to group endpoint */
    fun getGroups(): List<Group> {
        val groupsResponse = URL("${serviceEndpoint}groups").readText()
        return Json.decodeFromString(groupsResponse)
    }
}
