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

class AdministratorsCommand(private val api: ApolloClient, private val administratorsId: Int): Command("administrators", "Shows list of wiki administrators") {

    override fun onCommand(event: SlashCommandInteractionEvent) {
        runBlocking {
            val response: ApolloResponse<GroupsQuery.Data> = api.query(GroupsQuery(administratorsId)).execute()
            if(response.data?.groups?.single == null) {
                event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder().setColor(Color.RED).setTitle("Administrators not found").setDescription("Role of administrators not found").build()).queue()
                return@runBlocking
            }

            var description = ""
            response.data?.groups?.single?.users?.forEach { result ->
                description += "- ${result?.name}\n"
            }
            event.hook.setEphemeral(false).sendMessageEmbeds(EmbedBuilder().setColor(Constants.PRIMARY_COLOR).setTitle("Administrators").addField("Result", description, false).build()).queue()
        }
    }
}