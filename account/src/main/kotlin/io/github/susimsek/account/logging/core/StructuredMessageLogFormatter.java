package io.github.susimsek.account.logging.core;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.messaging.MessageHeaders;

public interface StructuredMessageLogFormatter extends MessageLogFormatter {

    @Override
    default String format(final Message message)
        throws IOException {
        return format(prepare(message));
    }

    @Override
    default String format(final Message message, final boolean sent) throws IOException {
        return format(prepare(message, sent));
    }

    String format(Map<String, Object> content) throws IOException;

    default Map<String, Object> prepare(final Message message)
        throws IOException {

        final Map<String, Object> content = new LinkedHashMap<>();
        content.put("type", "message");
        content.put("channelType", message.getChannelType().name().toLowerCase(Locale.ROOT));
        content.put("channel", message.getChannel());
        content.put("destination", message.getDestination());
        prepareHeaders(message).ifPresent(headers -> content.put("headers", headers));
        preparePayload(message).ifPresent(payload -> content.put("payload", payload));

        return content;
    }

    default Map<String, Object> prepare(final Message message, final boolean sent)
        throws IOException {

        final Map<String, Object> content = new LinkedHashMap<>();
        content.put("type", "message");
        content.put("channelType", message.getChannelType().name().toLowerCase(Locale.ROOT));
        content.put("channel", message.getChannel());
        content.put("destination", message.getDestination());
        content.put("sent", sent);
        prepareHeaders(message).ifPresent(headers -> content.put("headers", headers));
        preparePayload(message).ifPresent(payload -> content.put("payload", payload));

        return content;
    }

    default Optional<MessageHeaders> prepareHeaders(final Message message) {
        var headers = message.headers();
        return Optional.ofNullable(headers.isEmpty() ? null : headers);
    }

    default Optional<Object> preparePayload(final Message message) throws IOException {
        final String body = message.getPayloadAsString();
        return Optional.ofNullable(body.isEmpty() ? null : body);
    }

}