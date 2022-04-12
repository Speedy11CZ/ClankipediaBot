package cz.speedy.clankipediabot.command.wiki

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import cz.speedy.clankipediabot.api.PagesQuery
import cz.speedy.clankipediabot.command.Command
import cz.speedy.clankipediabot.util.Constants
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.awt.Color

class PagesCommand(private val api: ApolloClient, private val rootUrl: String): Command("pages", "Shows every page on the wiki") {

    override fun onCommand(event: SlashCommandInteractionEvent) {
        runBlocking {
            val response: ApolloResponse<PagesQuery.Data> = api.query(PagesQuery()).execute()
            if(response.data?.pages?.list?.isEmpty()!!) {
                event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder().setColor(Color.RED).setTitle("Emptiness").setDescription("There is no wiki page").build()).queue()
                return@runBlocking
            }

            var description = ""
            response.data?.pages?.list?.forEach { result ->
                var pageDescription: String? = result.description
                if(pageDescription?.length!! < 1) pageDescription = "no description"

                description += "- [${result.title}](${rootUrl}/${result.path}) - $pageDescription\n"
            }
            event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder().setColor(Constants.PRIMARY_COLOR).setTitle("Pages").setDescription(description,).build()).queue()
        }
    }
}