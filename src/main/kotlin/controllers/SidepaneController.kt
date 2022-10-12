package controllers

import models.Group
import models.InvalidateGroupViewEvent
import models.InvalidateGroupViewRequest
import tornadofx.Controller
import tornadofx.DrawerItem
import java.util.*

class SidepaneController() : Controller() {
    private var groups: List<Group> = listOf(Group("test-group1"),
        Group("test-group-2"),
        Group("test-group-3"))
    private var focusedGroup: Group? = null

    fun groups(): List<Group> {
        // These will be loaded from the Server using the by lazy directive, or cached
        return groups
    }

    fun focusedGroup(): Group? { return focusedGroup }

    fun focusGroup(focus : Group) {
        //send along custom filter query
        if (focus !in groups) return
        focusedGroup = focus
        fire(InvalidateGroupViewRequest(focus))
        println("fired group view request")
    }

    fun createGroup() {}

    fun deleteGroup() {}
}