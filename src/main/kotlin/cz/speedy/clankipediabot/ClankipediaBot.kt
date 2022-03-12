package cz.speedy.clankipediabot

import com.apollographql.apollo3.ApolloClient
import com.moandjiezana.toml.Toml
import cz.speedy.clankipediabot.api.AuthorizationInterceptor
import cz.speedy.clankipediabot.command.CommandHandler
import cz.speedy.clankipediabot.command.wiki.*
import cz.speedy.clankipediabot.listener.CommandListener
import cz.speedy.clankipediabot.util.ConfigUtil
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger("ClankipediaBot")

fun main() {
    logger.info("Starting ClankipediaBot")

    val config: Toml = ConfigUtil.loadConfig("config.toml")

    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl(config.getString("wiki.url") + "/graphql")
        .addInterceptor(AuthorizationInterceptor(config.getString("wiki.token")))
        .build()


    val jda: JDA = JDABuilder.createLight(config.getString("discord.token"))
        .setStatus(OnlineStatus.ONLINE)
        .setActivity(Activity.watching("Clankipedia"))
        .build().awaitReady()

    val commandHandler: CommandHandler = CommandHandler(jda)
        .registerCommand(PagesCommand(apolloClient, config.getString("wiki.url")))
        .registerCommand(SearchCommand(apolloClient, config.getString("wiki.url")))
        .registerCommand(ContributorsCommand(apolloClient, config.getLong("wiki.contributors").toInt()))
        .registerCommand(AdministratorsCommand(apolloClient, config.getLong("wiki.administrators").toInt()))
        .registerCommand(AboutCommand(apolloClient))
        .updateCommands()

    jda.addEventListener(CommandListener(commandHandler))

    logger.info("ClankipediaBot initialization completed")
}