package command

import commands.CommandFactory
import commands.ExitCommand
import commands.GetGroupsCommand
import commands.GetItemsCommand
import commands.GetLabelsCommand
import commands.HelpCommand
import commands.LoginUserCommand
import commands.RegisterUserCommand
import org.junit.jupiter.api.Test

class CommandFactoryTest {

    @Test
    fun testCreateHelpCommand() {
        assert(CommandFactory.createFromArgs(listOf("--help")).javaClass == HelpCommand::class.java)
    }

    @Test
    fun testCreateExitCommand() {
        assert(CommandFactory.createFromArgs(listOf("--exit")).javaClass == ExitCommand::class.java)
    }

    @Test
    fun testCreateRegisterUserCommand() {
        assert(CommandFactory.createFromArgs(listOf("--register")).javaClass == RegisterUserCommand::class.java)
    }

    @Test
    fun testCreateLoginUserCommand() {
        assert(CommandFactory.createFromArgs(listOf("--login")).javaClass == LoginUserCommand::class.java)
    }

    @Test
    fun testCreateGetItemsCommand() {
        assert(CommandFactory.createFromArgs(listOf("--items")).javaClass == GetItemsCommand::class.java)
    }

    @Test
    fun testCreateGetGroupsCommand() {
        assert(CommandFactory.createFromArgs(listOf("--groups")).javaClass == GetGroupsCommand::class.java)
    }

    @Test
    fun testCreateGetLabelsCommand() {
        assert(CommandFactory.createFromArgs(listOf("--labels")).javaClass == GetLabelsCommand::class.java)
    }
}
