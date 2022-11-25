package client

import bindings.GroupListProperty
import bindings.ItemListProperty
import bindings.LabelListProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
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

object TODOClient {
    private val itemList: ObservableList<Item> = FXCollections.observableArrayList()
    private val groupList: ObservableList<Group> = FXCollections.observableArrayList()
    private val labelList: ObservableList<Label> = FXCollections.observableArrayList()

    val itemListProperty = ItemListProperty(itemList)
    val groupListProperty = GroupListProperty(groupList)
    val labelListProperty = LabelListProperty(labelList)

    // TODO: This will change once we deploy the service to the cloud, this could also be a secret.
    private val serviceEndpoint = "http://localhost:8080/"
    private val client: HttpClient = HttpClient.newBuilder().build()
    private val successCode: Int = 200

    // used to cached current logged-in user.
    private var loggedInUser: User? = null

    fun init() {
        getLabels()
        getGroups()
        getItems()
    }

    /* Method related to cached logged-in user */
    fun getLoggedInUser(): User {
        if (loggedInUser == null) {
            throw IllegalArgumentException("This method should only be called once the user has logged in.")
        }
        return loggedInUser as User
    }

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
            throw IllegalArgumentException("User ${user.username} already exists")
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
            throw IllegalArgumentException("Invalid password for user ${user.username}")
        }
        loggedInUser = Json.decodeFromString(response.body())
    }

    fun logOutUser() {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}logout"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.noBody())
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
        loggedInUser = null
    }

    /* Methods related to item endpoint */
    fun getItems(): List<Item> {
        val itemsResponse = URL("${serviceEndpoint}items").readText()
        val newItemList: List<Item> = Json.decodeFromString(itemsResponse)

        itemList.setAll(newItemList)
        return itemListProperty.value
    }

    fun createItem(item: Item): Item {
        val string = Json.encodeToString(item)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}items"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        val itemResponse = client.send(request, HttpResponse.BodyHandlers.ofString())
        val newItem: Item = Json.decodeFromString(itemResponse.body())

        itemList.add(newItem)
        return newItem
    }

    fun editItem(id: Int, newItem: Item) {
        val string = Json.encodeToString(newItem)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}items/$id"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())

        itemList[itemList.indexOf(newItem)] = newItem
    }

    fun deleteItem(item: Item) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}items/${item.id}"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())

        itemList.remove(item)
    }

    /* Methods related to labels endpoint */
    fun getLabels(): List<Label> {
        val labelsResponse = URL("${serviceEndpoint}labels").readText()
        val newLabelList: List<Label> = Json.decodeFromString(labelsResponse)

        labelList.setAll(newLabelList)
        return labelListProperty.value
    }

    fun createLabel(label: Label): Label {
        val string = Json.encodeToString(label)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}labels"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        val labelResponse = client.send(request, HttpResponse.BodyHandlers.ofString())
        val newLabel: Label = Json.decodeFromString(labelResponse.body())

        labelList.add(newLabel)
        return newLabel
    }

    fun editLabel(id: Int, newLabel: Label) {
        val string = Json.encodeToString(newLabel)
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}labels/$id"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
        labelList[labelList.indexOf(newLabel)] = newLabel
    }

    // TODO: This will be used for the settings UI that does not currently exist
    fun deleteLabel(label: Label) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}labels/${label.id}"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())

        labelList.remove(label)
    }

    /* Methods related to group endpoint */
    fun getGroups(): List<Group> {
        val groupsResponse = URL("${serviceEndpoint}groups").readText()
        val newGroupList: List<Group> = Json.decodeFromString(groupsResponse)

        groupList.setAll(newGroupList)
        return groupListProperty.value
    }

    fun createGroup(group: Group): Group {
        val string = Json.encodeToString(group)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}groups"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()
        val groupResponse = client.send(request, HttpResponse.BodyHandlers.ofString())
        val newGroup: Group = Json.decodeFromString(groupResponse.body())

        groupList.add(newGroup)
        return newGroup
    }

    fun editGroup(id: Int, newGroup: Group) {
        val string = Json.encodeToString(newGroup)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}groups/$id"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(string))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())

        groupList[groupList.indexOf(newGroup)] = newGroup
    }

    fun deleteGroup(group: Group) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${serviceEndpoint}groups/${group.id}"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())

        groupList.remove(group)
    }
}
