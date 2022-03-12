package cz.speedy.clankipediabot.command

import net.dv8tion.jda.api.JDA

class CommandHandler(private val jda: JDA) {

    private val commands: MutableList<Command> = mutableListOf()

    fun getCommand(commandName: String): Command? {
        for (command in commands) {
            if(command.getName() == commandName) {
                return command
            }
        }
        return null
    }

    fun registerCommand(command: Command): CommandHandler {
        commands.add(command)
        return this
    }

    fun updateCommands(): CommandHandler {
        jda.updateCommands().addCommands(commands.map { command ->  command.getCommandData()}).queue()
        return this
    }

}