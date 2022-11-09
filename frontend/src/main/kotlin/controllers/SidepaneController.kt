package controllers

import TODOApplication
import client.TODOClient
import commands.CreateGroupCommand
import commands.DeleteGroupCommand
import commands.EditGroupCommand
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import models.Group
import views.SidepaneView

class SidepaneController(todoApp: TODOApplication) {
    private var app: TODOApplication? = null
    private var view: SidepaneView? = null
    private var groups: List<Group> = listOf()
    private val todoClient = TODOClient()

    init {
        app = todoApp
    }

    // Setting as a property so it can be listened to for changes by views
    private var focusedGroup: SimpleObjectProperty<Group> = SimpleObjectProperty<Group>()

    fun groups(): List<Group> {
        return groups
    }

    fun loadGroups() {
        groups = todoClient.getGroups()
    }

    fun refreshGroups() {
        view?.refreshGroups()
    }

    fun focusedGroup(): ObjectProperty<Group> { return focusedGroup }

    fun focusGroup(focus: Group?) {
        if (focus !in groups) return
        focusedGroup.set(focus)
        app?.groupViewController?.loadGroup(focusedGroup.value)
    }

    fun createGroup(group: Group) {
        val createGroupCommand = CreateGroupCommand(group, this)
        app?.commandHandler?.execute(createGroupCommand)
        view?.refreshGroups()
    }

    fun deleteGroup(group: Group) {
        val deleteGroupCommand = DeleteGroupCommand(group, this)
        app?.commandHandler?.execute(deleteGroupCommand)
        view?.refreshGroups()
    }

    // TODO: this should also refresh the currently loaded group view
    fun editGroup(newGroup: Group, originalGroup: Group) {
        val editGroupCommand = EditGroupCommand(newGroup, originalGroup, this)
        app?.commandHandler?.execute(editGroupCommand)
        view?.refreshGroups()
    }

    // view management
    fun addView(sidepaneView: SidepaneView) {
        view = sidepaneView
    }
}
