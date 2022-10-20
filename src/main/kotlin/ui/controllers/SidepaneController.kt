package ui.controllers

import TODOApplication
import models.Group
import ui.client.TODOClient
import ui.views.SidepaneView

class SidepaneController(todoApp: TODOApplication) {
    private var app: TODOApplication? = null
    private var view: SidepaneView? = null
    private var groups: List<Group> = listOf()
    private val todoClient = TODOClient()

    init {
        app = todoApp
        groups = todoClient.getGroups()
    }
    private var focusedGroup: Group? = null

    fun groups(): List<Group> {
        return groups
    }

    fun focusedGroup(): Group? { return focusedGroup }

    fun focusGroup(focus: Group) {
        if (focus !in groups) return
        focusedGroup = focus
        app?.groupViewController?.loadGroup(focusedGroup)
    }

    fun createGroup() {}

    fun deleteGroup() {}

    // view management
    fun addView(sidepaneView: SidepaneView) {
        view = sidepaneView
    }
}
