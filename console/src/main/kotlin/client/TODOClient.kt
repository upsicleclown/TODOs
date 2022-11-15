package client

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Group
import models.Item
import models.Label
import models.User
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class TODOClient {

    // TODO: This will change once we deploy the service to the cloud, this could also be a secret.
    private val serviceEndpoint = "http://localhost:8080/"
    private val client: HttpClient = HttpClient.newBuilder().build()
    private val successCode: Int = 200

    /* Methods related to user endpoint */
    fun registerUser(user: User) {
        val string = Json.encodeToString(user)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}register"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != successCode) {
            throw IllegalArgumentException("User ${user.username} already exists.")
        }
    }

    fun logInUser(user: User) {
        val string = Json.encodeToString(user)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}user"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != successCode) {
            throw IllegalArgumentException("Invalid password for user ${user.username}.")
        }
    }

    fun logOutUser() {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}logout"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.noBody())
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    /* Methods related to item endpoint */
    fun getItems(): List<Item> {
        val itemsResponse = URL("${serviceEndpoint}items").readText()
        return Json.decodeFromString(itemsResponse)
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
}
