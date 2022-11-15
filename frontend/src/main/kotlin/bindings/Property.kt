package bindings
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import models.Group
import models.Item
import models.Label

class ItemProperty() : SimpleObjectProperty<Item>()

class ItemListProperty(itemList: ObservableList<Item>) : SimpleListProperty<Item>(itemList)

class GroupProperty() : SimpleObjectProperty<Group>()

class GroupListProperty(groupList: ObservableList<Group>) : SimpleListProperty<Group>(groupList)

class LabelProperty() : SimpleObjectProperty<Label>()

class LabelListProperty(labelList: ObservableList<Label>) : SimpleListProperty<Label>(labelList)
