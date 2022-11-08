package todo.service

import org.springframework.stereotype.Service
import todo.database.SQLiteDB

@Service
class Service {

    //
    // Item endpoints.
    //

    /**
     * Adds the provided item.
     *
     * @throws IllegalArgumentException if any of the labels in item do not exist.
     */
    fun addItem(item: models.Item): models.Item {
        return SQLiteDB.addItem(item)
    }

    /**
     * Item with provided id will now become new item. Hence, ensure new item has all fields it needs to have.
     *
     * @throws IllegalArgumentException if no such item with provided id or if any of the labels in item do not exist.
     */
    fun editItem(itemId: Int, newItem: models.Item) {
        SQLiteDB.editItem(itemId, newItem)
    }

    /**
     * Deletes the item with the provided id.
     *
     * @throws NoSuchElementException if no such item with provided id.
     */
    fun removeItem(itemId: Int) {
        SQLiteDB.removeItem(itemId)
    }

    /**
     * Returns all items in the database.
     */
    fun getItems(): List<models.Item> {
        return SQLiteDB.getItems()
    }

    //
    // Label endpoints
    //

    /**
     * Adds the provided label.
     */
    fun addLabel(label: models.Label): models.Label {
        return SQLiteDB.addLabel(label)
    }

    /**
     * Label with provided id will now become new label. Hence, ensure new label has all fields it needs to have.
     *
     * @throws NoSuchElementException if no such label with provided id.
     */
    fun editLabel(labelId: Int, newLabel: models.Label) {
        SQLiteDB.editLabel(labelId, newLabel)
    }

    /**
     * Deletes the label with the provided id.
     *
     * @throws IllegalArgumentException if no such label with provided id.
     */
    fun removeLabel(labelId: Int) {
        SQLiteDB.removeLabel(labelId)
    }

    /**
     * Returns all label in the database.
     */
    fun getLabels(): List<models.Label> {
        return SQLiteDB.getLabels()
    }

    //
    // Group endpoints
    //

    /**
     * Adds the provided group.
     *
     * @throws IllegalArgumentException if any of the labels in group do not exist.
     */
    fun addGroup(group: models.Group): models.Group {
        return SQLiteDB.addGroup(group)
    }

    /**
     * Group with provided id will now become new group. Hence, ensure new group has all fields it needs to have.
     *
     * @throws IllegalArgumentException if no such item with provided id or if any of the labels in item do not exist.
     */
    fun editGroup(groupId: Int, newGroup: models.Group) {
        SQLiteDB.editGroup(groupId, newGroup)
    }

    /**
     * Deletes the group with the provided id.
     *
     * @throws IllegalArgumentException if no such group with provided id.
     */
    fun removeGroup(groupId: Int) {
        SQLiteDB.removeGroup(groupId)
    }

    fun getGroups(): List<models.Group> {
        return SQLiteDB.getGroups()
    }
}
