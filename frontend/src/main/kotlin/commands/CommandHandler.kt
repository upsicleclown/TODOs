package commands

class CommandHandler {
    var executedCommandsStack = ArrayDeque<Command>()
    var undoneCommandsStack = ArrayDeque<Command>()

    fun execute(command: Command) {
        command.execute()
        executedCommandsStack.addLast(command)
        undoneCommandsStack.clear()
    }

    fun undo() {
        var command = executedCommandsStack.removeLastOrNull()
        if (command != null) {
            command.undo()
            undoneCommandsStack.addLast(command)
        }
    }

    fun redo() {
        var command = undoneCommandsStack.removeLastOrNull()
        command?.redo()
    }
}
