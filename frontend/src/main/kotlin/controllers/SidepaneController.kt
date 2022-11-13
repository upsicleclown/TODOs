package controllers

import TODOApplication
import bindings.GroupListProperty
import bindings.GroupProperty
import client.TODOClient
import commands.CreateGroupCommand
import commands.DeleteGroupCommand
import commands.EditGroupCommand
import models.Group
import views.SidepaneView

class SidepaneController(todoApp: TODOApplication) {
    private var app: TODOApplication? = null
    private var view: SidepaneView? = null
    private val todoClient = TODOClient
    var groupListProperty: GroupListProperty
    var focusedGroupProperty = GroupProperty()

    init {
        app = todoApp
        groupListProperty = todoClient.groupListProperty
    }

    fun focusGroup(focus: Group?) {
        if (focus !in groupListProperty) return
        focusedGroupProperty.set(focus)
        app?.groupViewController?.loadGroup(focusedGroupProperty.value)
    }

    fun createGroup(group: Group) {
        val createGroupCommand = CreateGroupCommand(group)
        app?.commandHandler?.execute(createGroupCommand)
    }

    fun deleteGroup(group: Group) {
        val deleteGroupCommand = DeleteGroupCommand(group)
        app?.commandHandler?.execute(deleteGroupCommand)
    }

    fun editGroup(newGroup: Group, originalGroup: Group) {
        val editGroupCommand = EditGroupCommand(newGroup, originalGroup)
        app?.commandHandler?.execute(editGroupCommand)
    }

    fun reloadGroupCreationView() {
        view?.groupCreationDialog?.refreshLabels()
    }

    // view management
    fun addView(sidepaneView: SidepaneView) {
        view = sidepaneView
    }
}
