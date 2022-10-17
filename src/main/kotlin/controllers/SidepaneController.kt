package controllers

import TODOApplication
import javafx.application.Application
import models.Group
import views.GroupView
import views.SidepaneView

class SidepaneController(todoApp: TODOApplication)  {
    private var app: TODOApplication? = null
    private var view: SidepaneView? = null

    init {
        app = todoApp
    }
    private var groups: List<Group> = listOf(Group("test-group1"),
        Group("test-group-2"),
        Group("test-group-3"))
    private var focusedGroup: Group? = null

    /* TODO: should be loaded from server in init builder */
    fun groups(): List<Group> {
        // These will be loaded from the Server using the by lazy directive, or cached
        return groups
    }

    fun focusedGroup(): Group? { return focusedGroup }

    fun focusGroup(focus : Group) {
        if (focus !in groups) return
        focusedGroup = focus
        app?.groupViewController?.loadGroup(focusedGroup)
//        fire(InvalidateGroupViewRequest(focus))
    }

    fun createGroup() {}

    fun deleteGroup() {}

    // view management
    fun addView(sidepaneView: SidepaneView) {
        view = sidepaneView
    }
}
