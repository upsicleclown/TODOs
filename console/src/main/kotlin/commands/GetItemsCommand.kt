package commands

import client.TODOClient
import models.Item
import java.io.IOException

class GetItemsCommand(private val args: List<String>) : Command {

    companion object {
        const val usage = "To list all items, run as follows: '--items'."
    }

    override fun execute() {
        assert(args[0] == "--items")
        if (args.size != 1) {
            throw IllegalArgumentException(usage)
        }
        try {
            val items: List<Item> = TODOClient().getItems()
            println("***** Items in database *****")
            for (item in items) {
                println(item)
            }
            println()
        } catch (ignore: IOException) {
            ErrorCommand.print("You must first login to list groups.")
        }
    }
}
