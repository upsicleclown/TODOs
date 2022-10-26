package commands

/**
 *  Command is an interface that all other types of commands will inherit from
 */
interface Command {
    fun execute()
    fun undo()
    fun redo()
}
