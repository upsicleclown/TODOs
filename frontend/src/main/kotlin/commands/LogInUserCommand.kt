package commands

import client.TODOClient
import models.User

class LogInUserCommand(private val user: User) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.logInUser(user)
    }

    override fun undo() {
    }

    override fun redo() {
    }
}
