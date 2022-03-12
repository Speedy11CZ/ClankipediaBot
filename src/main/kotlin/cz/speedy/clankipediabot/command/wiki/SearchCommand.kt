package cz.speedy.clankipediabot.command.wiki

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import cz.speedy.clankipediabot.api.SearchQuery
import cz.speedy.clankipediabot.command.Command
import cz.speedy.clankipediabot.util.Constants
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.awt.Color

class SearchCommand(private val api: ApolloClient, private val rootUrl: String): Command("search", "Finds a page on the wiki") {

    init {
        options.add(OptionData(OptionType.STRING, "query", "search query").setRequired(true))
    }

    override fun onCommand(event: SlashCommandInteractionEvent) {
        val query: String = event.getOption("query")?.asString!!

        runBlocking {
            val response: ApolloResponse<SearchQuery.Data> = api.query(SearchQuery(query)).execute()
            if(response.data?.pages?.search?.totalHits!! < 1) {
                event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder().setColor(Color.RED).setTitle("No match").setDescription("No match found on the wiki").build()).queue()
                return@runBlocking
            }

            var description = ""
            response.data?.pages?.search?.results?.forEach { result ->
                var pageDescription: String? = result?.description
                if(pageDescription?.isEmpty()!!) pageDescription = "no description"

                description += "- [${result?.title}](${rootUrl}/${result?.path}) - $pageDescription\n"
            }
            event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder().setColor(Constants.PRIMARY_COLOR).setTitle("Search").addField("Query", query, false).addField("Result", description, false).build()).queue()
        }
    }
}