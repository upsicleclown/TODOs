package commands

class CommandHandler {
    var executedCommandsStack = ArrayDeque<Command>()
    var undoneCommandsStack = ArrayDeque<Command>()

    fun execute(command: Command) {
        command.execute()
        executedCommandsStack.addLast(command)
        // follows normal undo functionality, once new commands are run, undoing previous commands may cause conflicts
        undoneCommandsStack.clear()
    }

    fun undo() {
        val command = executedCommandsStack.removeLastOrNull()
        if (command != null) {
            command.undo()
            undoneCommandsStack.addLast(command)
        }
    }

    fun redo() {
        val command = undoneCommandsStack.removeLastOrNull()
        if (command != null) {
            command.redo()
            executedCommandsStack.addLast(command)
        }
    }
}
