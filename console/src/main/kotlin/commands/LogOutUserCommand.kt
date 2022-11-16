package commands

import client.TODOClient
import models.User

class LogOutUserCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To logout user, run as follows: '--logout'."
    }

    override fun execute(user: User?): User? {
        assert(args[0] == "--logout")
        if (args.size != 1) {
            ErrorCommand.print(usage)
            return user
        }
        TODOClient().logOutUser()
        println("User '${user!!.username}' logged out.")
        println()
        return user
    }
}
