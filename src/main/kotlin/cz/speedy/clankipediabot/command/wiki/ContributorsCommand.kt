package cz.speedy.clankipediabot.command.wiki

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import cz.speedy.clankipediabot.api.GroupsQuery
import cz.speedy.clankipediabot.command.Command
import cz.speedy.clankipediabot.util.Constants
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.awt.Color

class ContributorsCommand(private val api: ApolloClient, private val contributorsId: Int): Command("contributors", "Shows list of wiki contributors") {

    override fun onCommand(event: SlashCommandInteractionEvent) {
        runBlocking {
            val response: ApolloResponse<GroupsQuery.Data> = api.query(GroupsQuery(contributorsId)).execute()
            if(response.data?.groups?.single == null) {
                event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder().setColor(Color.RED).setTitle("Contributors not found").setDescription("Role of contributors not found").build()).queue()
                return@runBlocking
            }

            var description = ""
            response.data?.groups?.single?.users?.forEach { result ->
                description += "- ${result?.name}\n"
            }
            event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder().setColor(Constants.PRIMARY_COLOR).setTitle("Contributors").addField("Result", description, false).build()).queue()
        }
    }
}