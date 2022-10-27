package commands

import models.Item
import models.WindowSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommandTest {

    var commandHandler = CommandHandler()

    /*
       Tests related to commands.
     */
    @Test
    fun createItemCommandTest() {
        var item1 = Item("item1", false)
        val createItemCommand = CreateItemCommand(item1)
//        commandHandler.execute(createItemCommand)
//        assertEquals((commandHandler.executedCommandsStack.last() as CreateItemCommand).item, item1)
//
//        commandHandler.undo()
//        assertNull(commandHandler.executedCommandsStack.last())
//
//        commandHandler.redo()
//        assertEquals((commandHandler.executedCommandsStack.last() as CreateItemCommand).item, item1)
    }

    @Test
    fun editItemCommandTest() {
        var item1 = Item("item1", false)
        var item2 = Item("item2", false)
        val createItemCommand = CreateItemCommand(item1)
//        val editItemCommand = EditItemCommand(item1, item2)
//        commandHandler.execute(createItemCommand)
//        assertEquals((commandHandler.executedCommandsStack.last() as CreateItemCommand).item, item1)
//        commandHandler.execute(editItemCommand)
//        assertEquals((commandHandler.executedCommandsStack.last() as CreateItemCommand).item, item2)
//
//        commandHandler.undo()
//        assertEquals((commandHandler.executedCommandsStack.last() as CreateItemCommand).item, item1)
//
//        commandHandler.redo()
//        assertEquals((commandHandler.executedCommandsStack.last() as CreateItemCommand).item, item2)
    }

    @Test
    fun deleteItemCommandTest() {
        var item1 = Item("item1", false)
        val createItemCommand = CreateItemCommand(item1)
        val deleteItemCommand = DeleteItemCommand(item1)
//        commandHandler.execute(createItemCommand)
//        assertEquals((commandHandler.executedCommandsStack.last() as CreateItemCommand).item, item1)
//
//        commandHandler.execute(deleteItemCommand)
//        assertNull(commandHandler.executedCommandsStack.last())
//
//        commandHandler.undo()
//        assertEquals((commandHandler.executedCommandsStack.last() as CreateItemCommand).item, item1)
//
//        commandHandler.redo()
//        assertNull(commandHandler.executedCommandsStack.last())
    }
}
