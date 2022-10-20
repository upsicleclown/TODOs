package ui.controllers

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import models.Group
import models.Item
import ui.views.GroupView
import java.net.URL

class GroupViewController() {
    private var items = listOf<Item>()
    private var view: GroupView? = null

    init {
        val cachedItems = URL("http://localhost:8080/items").readText()
        items = Json.decodeFromString<List<Item>>(cachedItems)
    }

    fun loadGroup(group: Group?) {
        if (group === null) { return }
        view?.refreshWithItems(group, items)
    }

    // view management
    fun addView(groupView: GroupView) {
        view = groupView
    }
}
