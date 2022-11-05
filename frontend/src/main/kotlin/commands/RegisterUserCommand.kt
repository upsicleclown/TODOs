package commands

import client.TODOClient
import models.User

class RegisterUserCommand(private val user: User) : Command {
    private val todoClient = TODOClient()

    override fun execute() {
        todoClient.registerUser(user)
    }

    override fun undo() {
    }

    override fun redo() {
    }
}
