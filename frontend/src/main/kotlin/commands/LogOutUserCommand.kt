package commands

import client.TODOClient

class LogOutUserCommand : Command {
    private val todoClient = TODOClient

    override fun execute() {
        todoClient.logOutUser()
    }

    /**
     * Left un-implemented on purpose since we do not want to support undo-ing this command.
     */
    override fun undo() {
    }

    /**
     * Left un-implemented on purpose since we do not want to support redo-ing this command.
     */
    override fun redo() {
    }
}
