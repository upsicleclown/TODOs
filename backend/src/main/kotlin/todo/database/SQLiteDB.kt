package todo.database

import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import todo.database.models.Group
import todo.database.models.Item
import todo.database.models.Label
import todo.database.tables.GroupLabels
import todo.database.tables.Groups
import todo.database.tables.ItemLabels
import todo.database.tables.Items
import todo.database.tables.Labels

/*
   SQLite Database.

   Connection string could be a secret.
 */
open class SQLiteDB(connectionString: String = "jdbc:sqlite:todo.db") {
    /**
     * Constructor.
     *
     * Creates db tables if they do not exist.
     */
    init {
        Database.connect(connectionString)
        transaction {
            addLogger(StdOutSqlLogger)

            // create tables.
            SchemaUtils.create(Items)
            SchemaUtils.create(Groups)
            SchemaUtils.create(Labels)
            SchemaUtils.create(ItemLabels)
            SchemaUtils.create(GroupLabels)
        }
    }

    //
    // Methods related to items.
    //

    /**
     * Adds the provided item.
     *
     * @throws IllegalArgumentException if any of the labels in item do not exist.
     */
    fun addItem(item: models.Item) {
        // Ensure labels in item exist.
        val labelsInDB: List<Label> = getDbLabels()
        val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(item.labelIds, labelsInDB.map { label: Label -> label.id.value })
        if (labelIdsNotInDb.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot add item with label ids $labelIdsNotInDb " +
                    "since these label ids do not exist in the database."
            )
        }

        // Save item.
        val labelsToSave: List<Label> = labelsInDB.filter { label: Label -> item.labelIds.contains(label.id.value) }
        transaction {
            Item.new {
                title = item.title
                isComplete = item.isCompleted
                labels = SizedCollection(labelsToSave)
                edtDueDate = item.edtDueDate?.toJavaLocalDateTime() // assume local time is EDT
                priority = item.priority?.name
            }
        }
    }

    /**
     * Deletes the item with the provided id.
     *
     * @throws IllegalArgumentException if no such item with provided id.
     */
    fun removeItem(itemId: Int) {
        try {
            transaction {
                val oldItem: Item = Item.find { Items.id eq itemId }.first()
                oldItem.delete()
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not remove item with id $itemId since no such item in database.")
        }
    }

    /**
     * Item with provided id will now become new item. Hence, ensure new item has all fields it needs to have.
     *
     * @throws IllegalArgumentException if no such item with provided id or if any of the labels in item do not exist.
     */
    fun editItem(itemId: Int, newItem: models.Item) {
        // Ensure labels in item exist.
        val labelsInDB: List<Label> = getDbLabels()
        val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(newItem.labelIds, labelsInDB.map { label: Label -> label.id.value })
        if (labelIdsNotInDb.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot edit item with label ids $labelIdsNotInDb " +
                    "since these label ids do not exist in the database."
            )
        }

        // Edit item.
        val labelsToSave: List<Label> = labelsInDB.filter { label: Label -> newItem.labelIds.contains(label.id.value) }
        try {
            transaction {
                val oldItem: Item = Item.find { Items.id eq itemId }.first()
                oldItem.title = newItem.title
                oldItem.isComplete = newItem.isCompleted
                oldItem.labels = SizedCollection(labelsToSave)
                oldItem.edtDueDate = newItem.edtDueDate?.toJavaLocalDateTime() // assume local time is EDT
                oldItem.priority = newItem.priority?.name
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not edit item with id $itemId since no such item in database.")
        }
    }

    /**
     * Returns all items in the database.
     */
    fun getItems(): List<models.Item> {
        return transaction {
            Item.all().toList().map {
                models.Item(
                    it.title,
                    it.isComplete,
                    it.labels.map { label -> label.id.value } as MutableList<Int>,
                    it.edtDueDate?.toKotlinLocalDateTime(), // assume local time is EDT
                    it.priority?.let { itemPriority -> models.Priority.valueOf(itemPriority) },
                    it.id.value
                )
            }
        }
    }

    //
    // Private helpers related to labels.
    //

    /**
     * Given a list of labels ids of interest and the list of label ids in the db, returns
     * all the label ids of interest that are not in the db.
     */
    private fun getLabelIdsNotInDb(labelIds: List<Int>, labelIdsInDb: List<Int>): List<Int> {
        val labelIdsNotInDB = mutableListOf<Int>()
        for (labelId in labelIds) {
            if (!labelIdsInDb.contains(labelId)) {
                labelIdsNotInDB.add(labelId)
            }
        }
        return labelIdsNotInDB
    }

    /**
     * Returns all label in the database.
     */
    private fun getDbLabels(): List<Label> {
        return transaction {
            Label.all().toList()
        }
    }

    //
    // Methods related to labels.
    //

    /**
     * Adds the provided label.
     */
    fun addLabel(label: models.Label) {
        transaction {
            Label.new {
                name = label.name
                color = label.color
            }
        }
    }

    /**
     * Deletes the label with the provided id.
     *
     * @throws IllegalArgumentException if no such label with provided id.
     */
    fun removeLabel(labelId: Int) {
        try {
            transaction {
                // Remove label from items with this label
                val itemsWithLabel: List<Item> = Item.all().filter { it.labels.map { label -> label.id.value }.contains(labelId) }
                for (item in itemsWithLabel) {
                    item.labels = SizedCollection(item.labels.filter { it.id.value != labelId })
                }

                // Remove label from groups with this label
                val groupsWithLabel: List<Group> = Group.all().filter { it.labels.map { label -> label.id.value }.contains(labelId) }
                for (group in groupsWithLabel) {
                    group.labels = SizedCollection(group.labels.filter { it.id.value != labelId })
                }

                // Remove label.
                val oldLabel: Label = Label.find { Labels.id eq labelId }.first()
                oldLabel.delete()
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not edit label with id $labelId since no such label in database.")
        }
    }

    /**
     * Label with provided id will now become new label. Hence, ensure new label has all fields it needs to have.
     *
     * @throws IllegalArgumentException if no such label with provided id.
     */
    fun editLabel(labelId: Int, newLabel: models.Label) {
        try {
            transaction {
                val oldLabel: Label = Label.find { Labels.id eq labelId }.first()
                oldLabel.name = newLabel.name
                oldLabel.color = newLabel.color
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not edit label with id $labelId since no such label in database.")
        }
    }

    /**
     * Returns all label in the database.
     */
    fun getLabels(): List<models.Label> {
        return getDbLabels().map { models.Label(it.name, it.color, it.id.value) }
    }

    //
    // Methods related to groups.
    //

    /**
     * Adds the provided group.
     */
    fun addGroup(group: models.Group) {
        // Ensure labels in group exist.
        val labelsInDB: List<Label> = getDbLabels()
        val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(group.labelIds, labelsInDB.map { label: Label -> label.id.value })
        if (labelIdsNotInDb.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot add group with label ids $labelIdsNotInDb " +
                    "since these label ids do not exist in the database."
            )
        }

        // Save group.
        val labelsToSave: List<Label> = labelsInDB.filter { label: Label -> group.labelIds.contains(label.id.value) }
        transaction {
            Group.new {
                name = group.name
                labels = SizedCollection(labelsToSave)
            }
        }
    }

    /**
     * Deletes the group with the provided id.
     *
     * @throws IllegalArgumentException if no such group with provided id.
     */
    fun removeGroup(groupId: Int) {
        try {
            transaction {
                val oldGroup: Group = Group.find { Groups.id eq groupId }.first()
                oldGroup.delete()
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not remove group with id $groupId since no such group in database.")
        }
    }

    /**
     * Group with provided id will now become new group. Hence, ensure new group has all fields it needs to have.
     *
     * @throws IllegalArgumentException if no such group with provided id or if any of the labels in group do not exist.
     */
    fun editGroup(groupId: Int, newGroup: models.Group) {
        // Ensure labels in group exist.
        val labelsInDB: List<Label> = getDbLabels()
        val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(newGroup.labelIds, labelsInDB.map { label: Label -> label.id.value })
        if (labelIdsNotInDb.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot edit group with label ids $labelIdsNotInDb " +
                    "since these label ids do not exist in the database."
            )
        }

        // Edit group.
        val labelsToSave: List<Label> = labelsInDB.filter { label: Label -> newGroup.labelIds.contains(label.id.value) }
        try {
            transaction {
                val oldGroup: Group = Group.find { Groups.id eq groupId }.first()
                oldGroup.name = newGroup.name
                oldGroup.labels = SizedCollection(labelsToSave)
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not edit group with id $groupId since no such group in database.")
        }
    }

    /**
     * Returns all groups in the database.
     */
    fun getGroups(): List<models.Group> {
        return transaction {
            Group.all().toList().map {
                models.Group(
                    it.name,
                    it.labels.map { label -> label.id.value } as MutableList<Int>,
                    it.id.value
                )
            }
        }
    }
}
