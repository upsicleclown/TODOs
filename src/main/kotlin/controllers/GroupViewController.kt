package controllers

import TODOApplication
import models.Group
import models.Item
import views.GroupView

class GroupViewController(app: TODOApplication) {
    private var items = listOf<Item>()
    private var view: GroupView? = null

    fun loadGroup(group: Group?) {
        if (group === null) {return}
        /* Should be used for fetching group info from server necessary to render */
        items = listOf(Item("Task 1", isCompleted = true), Item("Task 2", isCompleted = false))
        view?.refreshWithItems(items)
    }

    // view management
    fun addView(groupView: GroupView) {
        view = groupView
    }
}