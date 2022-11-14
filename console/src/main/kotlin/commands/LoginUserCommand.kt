package commands

import client.TODOClient
import models.User

class LoginUserCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To login user, run as follows: '--login [username] [password]'."
    }

    override fun execute() {
        assert(args[0] == "--login")
        if (args.size != 3) {
            throw IllegalArgumentException(usage)
        }
        TODOClient().logInUser(User(args[1], args[2]))
        println("User '${args[1]}' logged in.")
        println()
    }
}
