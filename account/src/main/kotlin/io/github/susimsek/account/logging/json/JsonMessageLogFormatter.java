package io.github.susimsek.account.logging.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.susimsek.account.logging.core.Message;
import io.github.susimsek.account.logging.core.StructuredMessageLogFormatter;
import io.github.susimsek.account.logging.utils.HeaderUtils;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public final class JsonMessageLogFormatter implements StructuredMessageLogFormatter {

    private final ObjectMapper om;

    public JsonMessageLogFormatter(ObjectMapper mapper) {
        var om = mapper.copy();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        this.om = om;
    }

    @Override
    public Optional<Object> preparePayload(final Message message) throws IOException {
        var contentType = message.getContentType();
        var payload = message.getPayloadAsString();
        if (payload.isEmpty()) {
            return Optional.empty();
        }
        if (contentType != null && HeaderUtils.isMessageInJsonFormat(contentType)) {
            return Optional.of(om.readValue(payload, Object.class));
        } else {
            return Optional.of(payload);
        }
    }

    public String format(final Map<String, Object> content) throws IOException {
        return om.writeValueAsString(content);
    }
}