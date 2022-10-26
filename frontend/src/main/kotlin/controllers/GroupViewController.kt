package controllers

import TODOApplication
import client.TODOClient
import models.Group
import models.Item
import views.GroupView

class GroupViewController(todoApp: TODOApplication) {
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
