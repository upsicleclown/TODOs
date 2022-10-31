package controllers

import TODOApplication
import client.TODOClient
import commands.CreateGroupCommand
import commands.DeleteGroupCommand
import commands.EditGroupCommand
import models.Group
import views.SidepaneView

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

    fun loadGroups() {
        groups = todoClient.getGroups()
    }

    fun focusedGroup(): Group? { return focusedGroup }

    fun focusGroup(focus: Group?) {
        if (focus !in groups) return
        focusedGroup = focus
        app?.groupViewController?.loadGroup(focusedGroup)
    }

    fun createGroup(group: Group) {
        val createGroupCommand = CreateGroupCommand(group)
        app?.commandHandler?.execute(createGroupCommand)
        view?.refreshGroups()
    }

    fun deleteGroup(group: Group) {
        val deleteGroupCommand = DeleteGroupCommand(group)
        app?.commandHandler?.execute(deleteGroupCommand)
        view?.refreshGroups()
    }

    fun editGroup(newGroup: Group, originalGroup: Group) {
        val editGroupCommand = EditGroupCommand(newGroup, originalGroup)
        app?.commandHandler?.execute(editGroupCommand)
        view?.refreshGroups()
    }

    // view management
    fun addView(sidepaneView: SidepaneView) {
        view = sidepaneView
    }
}
