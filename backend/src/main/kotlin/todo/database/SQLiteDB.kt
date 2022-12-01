package todo.database

import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import todo.database.models.BooleanOperator
import todo.database.models.Filter
import todo.database.models.Group
import todo.database.models.Item
import todo.database.models.Label
import todo.database.models.Priority
import todo.database.models.User
import todo.database.tables.Groups
import todo.database.tables.Items
import todo.database.tables.Labels
import todo.database.tables.Users
import models.BooleanOperator as BooleanOperatorEnum
import models.Priority as PriorityEnum

/*
   SQLite Database.
   Used to interface between database layer and Service layer.
   Using singleton pattern so all services can share the same database state.

 */
object SQLiteDB {
    // Service currently supports multi-user data but does not support  multiple users using the service simultaneously
    private var userLoggedIn: User? = null

    //
    // Methods related to users.
    //

    /**
     * Sets the current user logged in.
     */
    fun setUserLoggedIn(user: models.User): models.User {
        transaction {
            userLoggedIn = User.find { Users.username eq user.username }.first()
        }
        user.id = userLoggedIn!!.id.value
        return user
    }

    /**
     * Resets the current user logged in to null.
     */
    fun resetUserLoggedIn() {
        userLoggedIn = null
    }

    /**
     * Adds the provided user.
     *
     * @throws IllegalArgumentException if username already exists.
     */
    fun addUser(newUser: models.User, newPasswordHash: String) {
        transaction {
            val usersWithSameUsername: List<User> = User.find { (Users.username eq newUser.username) }.toList()
            if (usersWithSameUsername.isNotEmpty()) {
                throw IllegalArgumentException("Username ${newUser.username} already exists.")
            }
            User.new {
                username = newUser.username
                passwordHash = newPasswordHash
            }
        }
    }

    /**
     * Gets the provided user.
     *
     * @throws IllegalArgumentException if username does not exist or password does not match.
     */
    fun getUser(user: models.User, passwordHash: String): models.User {
        return transaction {
            try {
                val loadedUser: User = User.find { (Users.username eq user.username) and (Users.passwordHash eq passwordHash) }.first()
                models.User(loadedUser.username, user.password)
            } catch (noSuchElementException: NoSuchElementException) {
                throw IllegalArgumentException("No such user with username ${user.username} and password hash $passwordHash.")
            }
        }
    }

    /**
     * Deletes the user with the provided username.
     *
     * @throws IllegalArgumentException if no such user with provided id.
     */
    fun removeUser(username: String) {
        try {
            transaction {
                val oldUser: User = User.find { Users.username eq username }.first()
                oldUser.delete()
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not remove user $username since no such user in database.")
        }
    }

    //
    // Methods related to items.
    //

    /**
     * Adds the provided item for the logged-in user.
     *
     * @throws IllegalArgumentException if any of the labels in item do not exist.
     */
    fun addItem(item: models.Item): models.Item {
        // Ensure labels in item exist.
        val labelsInDB: List<Label> = getDbLabels()
        val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(item.labelIds, labelsInDB.map { label: Label -> label.id.value })
        if (labelIdsNotInDb.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot add item with label ids $labelIdsNotInDb " +
                    "since these label ids do not exist in the database for logged-in user ${userLoggedIn!!.username}."
            )
        }

        // Save item.
        val labelsToSave: List<Label> = labelsInDB.filter { label: Label -> item.labelIds.contains(label.id.value) }

        val insertedItem = transaction {
            Item.new {
                title = item.title
                isComplete = item.isCompleted
                labels = SizedCollection(labelsToSave)
                edtDueDate = item.edtDueDate // assume local time is EDT
                priority = item.priority?.let { Priority[it.name].id }
                user = userLoggedIn!!
            }
        }
        item.id = insertedItem.id.value
        return item
    }

    /**
     * Deletes the item with the provided id for the logged-in user.
     *
     * @throws IllegalArgumentException if no such item with provided id for the logged-in user.
     */
    fun removeItem(itemId: Int) {
        try {
            transaction {
                val oldItem: Item = Item.find { Items.id eq itemId }
                    .first { item: Item -> item.user.username == userLoggedIn!!.username }
                oldItem.delete()
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not remove item with id $itemId since no such item in database for logged-in user ${userLoggedIn!!.username}.")
        }
    }

    /**
     * Item with provided id will now become new item. Hence, ensure new item has all fields it needs to have.
     *
     * @throws IllegalArgumentException if no such item with provided id for the logged-in user or if any of the labels in item do not exist.
     */
    fun editItem(itemId: Int, newItem: models.Item) {
        // Ensure labels in item exist.
        val labelsInDB: List<Label> = getDbLabels()
        val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(newItem.labelIds, labelsInDB.map { label: Label -> label.id.value })
        if (labelIdsNotInDb.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot edit item with label ids $labelIdsNotInDb " +
                    "since these label ids do not exist in the database for logged-in user ${userLoggedIn!!.username}."
            )
        }

        // Edit item.
        val labelsToSave: List<Label> = labelsInDB.filter { label: Label -> newItem.labelIds.contains(label.id.value) }
        try {
            transaction {
                val oldItem: Item = Item.find { Items.id eq itemId }
                    .first { item: Item -> item.user.username == userLoggedIn!!.username }
                oldItem.title = newItem.title
                oldItem.isComplete = newItem.isCompleted
                oldItem.labels = SizedCollection(labelsToSave)
                oldItem.edtDueDate = newItem.edtDueDate // assume local time is EDT
                oldItem.priority = newItem.priority?.let { Priority[it.name].id }
                oldItem.user = userLoggedIn!!
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not edit item with id $itemId since no such item in database for logged-in user ${userLoggedIn!!.username}")
        }
    }

    /**
     * Returns all items in the database for the logged-in user.
     */
    fun getItems(): List<models.Item> {
        return transaction {
            Item.all().filter { item: Item -> item.user.username == userLoggedIn!!.username }.map {
                models.Item(
                    it.title,
                    it.isComplete,
                    it.labels.map { label -> label.id.value } as MutableList<Int>,
                    it.edtDueDate, // assume local time is EDT
                    it.priority?.let { priority -> PriorityEnum.valueOf(priority.value) },
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
     * Returns all label in the database for the logged-in user.
     */
    private fun getDbLabels(): List<Label> {
        return transaction {
            Label.all().filter { label: Label -> label.user.username == userLoggedIn!!.username }
        }
    }

    //
    // Methods related to labels.
    //

    /**
     * Adds the provided label for the logged-in user.
     */
    fun addLabel(label: models.Label): models.Label {
        val insertedLabel = transaction {
            Label.new {
                name = label.name
                color = label.color
                user = userLoggedIn!!
            }
        }
        label.id = insertedLabel.id.value
        return label
    }

    /**
     * Deletes the label with the provided id for the logged-in user.
     *
     * @throws IllegalArgumentException if no such label with provided id for the logged-in user.
     */
    fun removeLabel(labelId: Int) {
        try {
            transaction {
                // Fetch label
                val oldLabel: Label = Label.find { Labels.id eq labelId }.first { label: Label -> label.user.username == userLoggedIn!!.username }

                // Remove label from items with this label
                val itemsWithLabel: List<Item> = Item.all().filter { it.labels.map { label -> label.id.value }.contains(labelId) }
                for (item in itemsWithLabel) {
                    item.labels = SizedCollection(item.labels.filter { it.id.value != labelId })
                }

                // Remove label from filters with this label
                val filtersWithLabel: List<Filter> = Filter.all().filter { it.labels.map { label -> label.id.value }.contains(labelId) }
                for (filter in filtersWithLabel) {
                    filter.labels = SizedCollection(filter.labels.filter { it.id.value != labelId })
                }

                // Remove label.
                oldLabel.delete()
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not edit label with id $labelId since no such label in database for logged-in user ${userLoggedIn!!.username}")
        }
    }

    /**
     * Label with provided id will now become new label. Hence, ensure new label has all fields it needs to have.
     *
     * @throws IllegalArgumentException if no such label with provided id for the logged-in user.
     */
    fun editLabel(labelId: Int, newLabel: models.Label) {
        try {
            transaction {
                val oldLabel: Label = Label.find { Labels.id eq labelId }.first { label: Label -> label.user.username == userLoggedIn!!.username }
                oldLabel.name = newLabel.name
                oldLabel.color = newLabel.color
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not edit label with id $labelId since no such label in database for logged-in user ${userLoggedIn!!.username}")
        }
    }

    /**
     * Returns all label in the database for logged-in user.
     */
    fun getLabels(): List<models.Label> {
        return getDbLabels().map { models.Label(it.name, it.color, it.id.value) }
    }

    //
    // Methods related to groups.
    //

    /**
     * Adds the provided group for logged-in user.
     */
    fun addGroup(group: models.Group): models.Group {
        val groupFilter: models.Filter = group.filter
        // Ensure labels in group exist.
        val labelsInDB: List<Label> = getDbLabels()
        val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(groupFilter.labelIds, labelsInDB.map { label: Label -> label.id.value })
        if (labelIdsNotInDb.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot add group that filters by label ids $labelIdsNotInDb " +
                    "since these label ids do not exist in the database for logged-in user ${userLoggedIn!!.username}"
            )
        }

        // Save group with filter
        val labelsToSave: List<Label> = labelsInDB.filter { label: Label -> groupFilter.labelIds.contains(label.id.value) }
        val insertedGroup = transaction {
            Group.new {
                name = group.name
                filter = Filter.new {
                    edtStartDateRange = groupFilter.edtStartDateRange
                    edtEndDateRange = groupFilter.edtEndDateRange
                    isComplete = groupFilter.isCompleted
                    priorities = SizedCollection(groupFilter.priorities.map { Priority[it.name] })
                    labels = SizedCollection(labelsToSave)
                    labelBooleanOperator = BooleanOperator[groupFilter.labelBooleanOperator.name].id
                    user = userLoggedIn!!
                }
            }
        }
        group.id = insertedGroup.id.value
        return group
    }

    /**
     * Deletes the group with the provided id for logged-in user.
     *
     * @throws IllegalArgumentException if no such group with provided id for logged-in user.
     */
    fun removeGroup(groupId: Int) {
        try {
            transaction {
                val oldGroup: Group = Group.find { Groups.id eq groupId }.first { group: Group -> group.user.username == userLoggedIn!!.username }
                oldGroup.filter.delete()
                oldGroup.delete()
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not remove group with id $groupId since no such group in database for logged-in user ${userLoggedIn!!.username}")
        }
    }

    /**
     * Group with provided id will now become new group. Hence, ensure new group has all fields it needs to have.
     *
     * @throws IllegalArgumentException if no such group with provided id for the logged-in user or if any of the labels in group do not exist.
     */
    fun editGroup(groupId: Int, newGroup: models.Group) {
        val newFilter: models.Filter = newGroup.filter
        // Ensure labels in filter exist.
        val labelsInDB: List<Label> = getDbLabels()
        val labelIdsNotInDb: List<Int> = getLabelIdsNotInDb(newFilter.labelIds, labelsInDB.map { label: Label -> label.id.value })
        if (labelIdsNotInDb.isNotEmpty()) {
            throw IllegalArgumentException(
                "Cannot edit group with label ids $labelIdsNotInDb " +
                    "since these label ids do not exist in the database for logged-in user ${userLoggedIn!!.username}"
            )
        }

        // Edit group.
        val labelsToSave: List<Label> = labelsInDB.filter { label: Label -> newFilter.labelIds.contains(label.id.value) }
        try {
            transaction {
                val oldGroup: Group = Group.find { Groups.id eq groupId }.first { group: Group -> group.user.username == userLoggedIn!!.username }
                val oldFilter: Filter = oldGroup.filter

                oldFilter.edtStartDateRange = newFilter.edtStartDateRange
                oldFilter.edtEndDateRange = newFilter.edtEndDateRange
                oldFilter.isComplete = newFilter.isCompleted
                oldFilter.labels = SizedCollection(labelsToSave)
                oldFilter.labelBooleanOperator = BooleanOperator[newFilter.labelBooleanOperator.name].id
                oldFilter.priorities = SizedCollection(newFilter.priorities.map { Priority[it.name] })

                oldGroup.name = newGroup.name
                oldGroup.user = userLoggedIn!!
            }
        } catch (noSuchElementException: NoSuchElementException) {
            throw IllegalArgumentException("Could not edit group with id $groupId since no such group in database for logged-in user ${userLoggedIn!!.username}")
        }
    }

    /**
     * Returns all groups in the database for logged-in user.
     */
    fun getGroups(): List<models.Group> {
        return transaction {
            Group.all().filter { group: Group -> group.user.username == userLoggedIn!!.username }.map {
                val filter = it.filter

                models.Group(
                    it.name,
                    models.Filter(
                        filter.edtStartDateRange,
                        filter.edtEndDateRange,
                        filter.isComplete,
                        filter.priorities.map { priority -> PriorityEnum.valueOf(priority.id.toString()) } as MutableList<models.Priority>,
                        filter.labels.map { label -> label.id.value } as MutableList<Int>,
                        BooleanOperatorEnum.valueOf(filter.labelBooleanOperator.value)
                    ),
                    it.id.value
                )
            }
        }
    }
}
