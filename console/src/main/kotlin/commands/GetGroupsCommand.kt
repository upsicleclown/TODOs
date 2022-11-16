package commands

import client.TODOClient
import models.Group
import models.User
import java.io.IOException

class GetGroupsCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To see the list of groups, run as follows: '--groups'."
    }

    override fun execute(user: User?): User? {
        assert(args[0] == "--groups")
        if (args.size != 1) {
            ErrorCommand.print(usage)
            return user
        }
        try {
            val groups: List<Group> = TODOClient().getGroups()
            println("***** [${user!!.username}] Groups in database *****")
            for (group in groups) {
                println(group)
            }
            println()
        } catch (ignore: IOException) {
            ErrorCommand.print("You must first login to see the list of groups.")
        }
        return user
    }
}
