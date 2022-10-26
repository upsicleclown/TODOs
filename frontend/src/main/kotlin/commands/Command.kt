package commands

/**
 *  Command is an interface that all other types of commands will inherit from
 *
 *  Each child class will implement execute in its own way. The parameters for this function
 *  will be set as fields through the constructor.
 */
interface Command {
    fun execute()
    fun undo()
    fun redo()
}
