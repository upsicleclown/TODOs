package commands

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommandTest {

    private var commandHandler = CommandHandler()

    /*
       Tests related to commands.
     */

    class TestCommand(newValue: Int) : Command {
        var value = newValue

        override fun execute() {
            this.value++
        }

        override fun undo() {
            this.value--
        }

        override fun redo() {
            this.value++
        }
    }

    @Test
    fun undoCommandTest() {
        val oldValue = 10
        val testCommand = TestCommand(oldValue)
        commandHandler.execute(testCommand)
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 1)
        commandHandler.execute(testCommand)
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 2)
        commandHandler.execute(testCommand)
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 3)

        commandHandler.undo()
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 2)
        commandHandler.undo()
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 1)
        commandHandler.undo()
        assertNull(commandHandler.executedCommandsStack.lastOrNull())
        commandHandler.execute(testCommand)
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 1)
        commandHandler.undo()
        assertNull(commandHandler.executedCommandsStack.lastOrNull())
    }

    @Test
    fun redoCommandTest() {
        val oldValue = 10
        val testCommand = TestCommand(oldValue)
        commandHandler.execute(testCommand)
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 1)
        commandHandler.execute(testCommand)
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 2)
        commandHandler.execute(testCommand)
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 3)

        commandHandler.undo()
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 2)
        assertEquals((commandHandler.undoneCommandsStack.last() as TestCommand).value, oldValue + 2)
        commandHandler.undo()
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 1)
        assertEquals((commandHandler.undoneCommandsStack.last() as TestCommand).value, oldValue + 1)
        commandHandler.redo()
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 2)
        assertEquals((commandHandler.undoneCommandsStack.last() as TestCommand).value, oldValue + 2)
        commandHandler.redo()
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 3)
        assertNull(commandHandler.undoneCommandsStack.lastOrNull())

        commandHandler.execute(testCommand)
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 4)
        assertNull(commandHandler.undoneCommandsStack.lastOrNull())
        commandHandler.redo()
        assertEquals((commandHandler.executedCommandsStack.last() as TestCommand).value, oldValue + 4)
        assertNull(commandHandler.undoneCommandsStack.lastOrNull())
    }
}
