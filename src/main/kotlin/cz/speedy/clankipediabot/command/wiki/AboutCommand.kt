package cz.speedy.clankipediabot.command.wiki

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import cz.speedy.clankipediabot.api.SiteQuery
import cz.speedy.clankipediabot.command.Command
import cz.speedy.clankipediabot.util.Constants
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class AboutCommand(private val api: ApolloClient): Command("about", "Info about Clankipedia") {

    override fun onCommand(event: SlashCommandInteractionEvent) {
        runBlocking {
            val response: ApolloResponse<SiteQuery.Data> = api.query(SiteQuery()).execute()
            event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder()
                .setColor(Constants.PRIMARY_COLOR).setTitle("About")
                .setAuthor(response.data?.site?.config?.title, response.data?.site?.config?.host)
                .setDescription(response.data?.site?.config?.description)
                .setThumbnail(response.data?.site?.config?.logoUrl)
                .build()).addActionRow(Button.link(response.data?.site?.config?.host!!, "Link")).queue()
        }

    }
}