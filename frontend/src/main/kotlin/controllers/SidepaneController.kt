package controllers

import TODOApplication
import bindings.GroupListProperty
import bindings.GroupProperty
import bindings.LabelListProperty
import client.TODOClient
import commands.CreateGroupCommand
import commands.CreateGroupCreationLabelCommand
import commands.CreateLabelCommand
import commands.DeleteGroupCommand
import commands.DeleteGroupCreationLabelCommand
import commands.EditGroupCommand
import commands.EditGroupCreationLabelCommand
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import models.Group
import models.Label
import views.SidepaneView

class SidepaneController(todoApp: TODOApplication) {
    private var app: TODOApplication? = null
    private var view: SidepaneView? = null
    private val todoClient = TODOClient
    private val groupCreationLabelList: ObservableList<Label> = FXCollections.observableArrayList()

    var groupCreationLabelListProperty = LabelListProperty(groupCreationLabelList)
    var groupListProperty: GroupListProperty
    var labelListProperty: LabelListProperty
    var focusedGroupProperty = GroupProperty()

    init {
        app = todoApp
        groupListProperty = todoClient.groupListProperty
        labelListProperty = todoClient.labelListProperty
    }

    fun focusGroup(focus: Group?) {
        focusedGroupProperty.set(focus)
        app?.groupViewController?.loadGroup(focusedGroupProperty.value)
    }

    fun createGroup(group: Group) {
        val createGroupCommand = CreateGroupCommand(group)
        app?.commandHandler?.execute(createGroupCommand)
    }

    fun deleteGroup(group: Group) {
        if (group == focusedGroupProperty.value) {
            focusGroup(null)
        }
        val deleteGroupCommand = DeleteGroupCommand(group)
        app?.commandHandler?.execute(deleteGroupCommand)
    }

    fun editGroup(newGroup: Group, originalGroup: Group) {
        val editGroupCommand = EditGroupCommand(newGroup, originalGroup)
        app?.commandHandler?.execute(editGroupCommand)
    }

    fun createLabel(existingLabel: Boolean, newLabel: Label) {
        val createLabelCommand = CreateLabelCommand(existingLabel, newLabel)
        app?.commandHandler?.execute(createLabelCommand)
    }

    fun createGroupCreationLabel(newLabel: Label) {
        val createGroupCreationLabelCommand = CreateGroupCreationLabelCommand(newLabel, this)
        app?.commandHandler?.execute(createGroupCreationLabelCommand)
    }

    fun editGroupCreationLabel(newLabel: Label, originalLabel: Label) {
        val editGroupCreationLabelCommand = EditGroupCreationLabelCommand(newLabel, originalLabel, this)
        app?.commandHandler?.execute(editGroupCreationLabelCommand)
    }

    fun deleteGroupCreationLabel(label: Label) {
        val deleteGroupCreationLabelCommand = DeleteGroupCreationLabelCommand(label, this)
        app?.commandHandler?.execute(deleteGroupCreationLabelCommand)
    }

    // view management
    fun addView(sidepaneView: SidepaneView) {
        view = sidepaneView
    }

    fun todoApp(): TODOApplication? {
        return app
    }
}
