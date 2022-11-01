package client

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Group
import models.Item
import models.Label
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class TODOClient {

    // TODO: This will change once we deploy the service to the cloud, this could also be a secret.
    private val serviceEndpoint = "http://localhost:8080/"
    private val client: HttpClient = HttpClient.newBuilder().build()

    /* Methods related to item endpoint */
    fun getItems(): List<Item> {
        val itemsResponse = URL("${serviceEndpoint}items").readText()
        return Json.decodeFromString(itemsResponse)
    }

    fun createItem(item: Item) {
        val string = Json.encodeToString(item)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}items"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun editItem(id: Int, newItem: Item) {
        val string = Json.encodeToString(newItem)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}items/$id"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun deleteItem(item: Item) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}items/${item.id}"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    /* Methods related to labels endpoint */
    fun getLabels(): List<Label> {
        val labelsResponse = URL("${serviceEndpoint}labels").readText()
        return Json.decodeFromString(labelsResponse)
    }

    /* Methods related to group endpoint */
    fun getGroups(): List<Group> {
        val groupsResponse = URL("${serviceEndpoint}groups").readText()
        return Json.decodeFromString(groupsResponse)
    }

    fun createGroup(group: Group) {
        val string = Json.encodeToString(group)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}groups"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun editGroup(id: Int, newGroup: Group) {
        val string = Json.encodeToString(newGroup)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}groups/$id"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun deleteGroup(group: Group) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}groups/${group.id}"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}
