package ui.controllers

import TODOApplication
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import models.Group
import ui.views.SidepaneView
import java.net.URL

class SidepaneController(todoApp: TODOApplication) {
    private var app: TODOApplication? = null
    private var view: SidepaneView? = null
    private var groups: List<Group> = listOf()

    init {
        app = todoApp
        val cachedGroups = URL("http://localhost:8080/groups").readText()
        groups = Json.decodeFromString<List<Group>>(cachedGroups)
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
