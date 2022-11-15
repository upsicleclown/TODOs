package commands

import client.TODOClient
import models.Group
import java.io.IOException

class GetGroupsCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To list all groups, run as follows: '--groups'."
    }

    override fun execute() {
        assert(args[0] == "--groups")
        if (args.size != 1) {
            ErrorCommand.print(usage)
            return
        }
        try {
            val groups: List<Group> = TODOClient().getGroups()
            println("***** Groups in database *****")
            for (group in groups) {
                println(group)
            }
            println()
        } catch (ignore: IOException) {
            ErrorCommand.print("You must first login to see the list of groups.")
        }
    }
}
