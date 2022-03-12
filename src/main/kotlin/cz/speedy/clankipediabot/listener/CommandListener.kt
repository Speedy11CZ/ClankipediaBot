package cz.speedy.clankipediabot.listener

import cz.speedy.clankipediabot.command.Command
import cz.speedy.clankipediabot.command.CommandHandler
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class CommandListener(private val commandHandler: CommandHandler): ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command: Command? = commandHandler.getCommand(event.name)

        if(command == null) {
            event.replyEmbeds(EmbedBuilder().setColor(Color.RED).setTitle("Unsupported command").setDescription("This command isn't implemented.").build()).setEphemeral(true).queue()
            return
        }

        event.deferReply(false).queue()
        command.onCommand(event)
    }
}