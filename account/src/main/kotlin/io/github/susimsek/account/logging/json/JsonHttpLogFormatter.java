package io.github.susimsek.account.logging.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.susimsek.account.logging.core.HttpMessage;
import io.github.susimsek.account.logging.core.StructuredHttpLogFormatter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public final class JsonHttpLogFormatter implements StructuredHttpLogFormatter {

    private final ObjectMapper om;

    public JsonHttpLogFormatter(ObjectMapper mapper) {
        var om = mapper.copy();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        this.om = om;
    }

    public Optional<Object> prepareBody(final HttpMessage message) throws IOException {
        var contentType = message.getContentType();
        var body = message.getBodyAsString();
        if (body.isEmpty()) {
            return Optional.empty();
        }
        if (contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            return Optional.of(om.readValue(body, Object.class));
        } else {
            return Optional.of(body);
        }
    }

    public String format(final Map<String, Object> content) throws IOException {
        return om.writeValueAsString(content);
    }
}