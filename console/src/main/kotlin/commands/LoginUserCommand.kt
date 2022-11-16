package commands

import client.TODOClient
import models.User

class LoginUserCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To login user, run as follows: '--login [username] [password]'."
    }

    override fun execute(user: User?): User? {
        assert(args[0] == "--login")
        if (args.size != 3) {
            ErrorCommand.print(usage)
            return user
        }
        var loggedInUser: User? = null
        try {
            loggedInUser = User(args[1], args[2])
            TODOClient().logInUser(loggedInUser)
            println("User '${args[1]}' logged in.")
            println()
        } catch (ignore: IllegalArgumentException) {
            ErrorCommand.print("Could not find user '${args[1]}' with the given credentials")
        }
        return loggedInUser
    }
}
