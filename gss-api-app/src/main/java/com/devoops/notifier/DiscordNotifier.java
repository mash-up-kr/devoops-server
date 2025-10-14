package com.devoops.notifier;


import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordNotifier implements NotifyPort {

    private final DiscordProperties properties;

    private final JDA jda;

    public DiscordNotifier(DiscordProperties discordProperties) {
        this.properties = discordProperties;
        this.jda = initializeJda(properties.getToken());
    }

    private JDA initializeJda(String token) {
        try {
            return JDABuilder.createDefault(token).build().awaitReady();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GssException(ErrorCode.DISCORD_JDA_EXCEPTION);
        }
    }

    @Override
    public void sendMessage(String message) {
        TextChannel channel = jda.getTextChannelById(properties.getChannelId());
        channel.sendMessage(message).queue();
    }
}
