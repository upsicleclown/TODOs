package commands

import client.TODOClient
import models.User

class RegisterUserCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To register user, run as follows: '--register [username] [password]'."
    }

    override fun execute(user: User?): User? {
        assert(args[0] == "--register")
        if (args.size != 3) {
            ErrorCommand.print(usage)
            return user
        }
        try {
            TODOClient().registerUser(User(args[1], args[2]))
            println("User '${args[1]}' registered.")
            println()
        } catch (ignore: IllegalArgumentException) {
            ErrorCommand.print("Could not register user '${args[1]}' since this user already exists.")
        }
        return user
    }
}
