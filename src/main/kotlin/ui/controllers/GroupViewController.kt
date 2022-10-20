package ui.controllers

import models.Group
import models.Item
import ui.client.TODOClient
import ui.views.GroupView

class GroupViewController() {
    private var items = listOf<Item>()
    private var view: GroupView? = null
    private val todoClient = TODOClient()

    init {
        items = todoClient.getItems()
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
