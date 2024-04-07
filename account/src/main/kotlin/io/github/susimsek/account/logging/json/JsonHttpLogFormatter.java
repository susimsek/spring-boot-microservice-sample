package io.github.susimsek.account.logging.json;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.susimsek.account.logging.core.HttpMessage;
import io.github.susimsek.account.logging.core.StructuredHttpLogFormatter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.stereotype.Component;

@Component
public final class JsonHttpLogFormatter implements StructuredHttpLogFormatter {

    private static final List<String> SUPPORTED_CONTENT_TYPES = List.of(
        APPLICATION_JSON_VALUE,
        APPLICATION_PROBLEM_JSON_VALUE);

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
        Predicate<String> contentTypePredicate = (c) -> SUPPORTED_CONTENT_TYPES.stream()
            .anyMatch(c::startsWith);
        if (contentType != null && contentTypePredicate.test(contentType)) {
            return Optional.of(om.readValue(body, Object.class));
        } else {
            return Optional.of(body);
        }
    }

    public String format(final Map<String, Object> content) throws IOException {
        return om.writeValueAsString(content);
    }
}