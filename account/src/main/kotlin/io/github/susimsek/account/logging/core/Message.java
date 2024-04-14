package io.github.susimsek.account.logging.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;

public interface Message {

    ChannelType getChannelType();

    MessageHeaders headers();

    @Nullable
    default String getContentType() {
        return headers().get(MessageHeaders.CONTENT_TYPE, String.class);
    }

    String getChannel();

    String getDestination();

    byte[] payload() throws IOException;

    default String getPayloadAsString() throws IOException {
        return new String(payload(), StandardCharsets.UTF_8);
    }
}