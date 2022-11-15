package commands

import client.TODOClient
import models.Item
import models.User
import java.io.IOException

class GetItemsCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To see the list of items, run as follows: '--items'."
    }

    override fun execute(user: User?): User? {
        assert(args[0] == "--items")
        if (args.size != 1) {
            ErrorCommand.print(usage)
            return user
        }
        try {
            val items: List<Item> = TODOClient().getItems()
            println("***** [${user!!.username}] Items in database *****")
            for (item in items) {
                println(item)
            }
            println()
        } catch (ignore: IOException) {
            ErrorCommand.print("You must first login to see the list of items.")
        }
        return user
    }
}
