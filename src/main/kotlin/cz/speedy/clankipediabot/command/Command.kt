package cz.speedy.clankipediabot.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

abstract class Command(private val name: String, private val description: String) {

    protected var options: MutableList<OptionData> = mutableListOf()

    abstract fun onCommand(event: SlashCommandInteractionEvent)

    fun getName(): String {
        return name
    }

    fun getCommandData(): SlashCommandData {
        return Commands.slash(name, description).addOptions(options)
    }
}