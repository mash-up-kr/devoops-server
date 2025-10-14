package com.devoops.notifier;

import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "discord")
public class DiscordProperties {

    private final String token;
    private final String channelId;

    public DiscordProperties(String token, String channelId) {
        validate(token);
        validate(channelId);
        this.token = token;
        this.channelId = channelId;
    }

    private void validate(String element) {
        if (element == null || element.isBlank()) {
            throw new GssException(ErrorCode.DISCORD_PROPERTIES_EMPTY);
        }
    }
}
