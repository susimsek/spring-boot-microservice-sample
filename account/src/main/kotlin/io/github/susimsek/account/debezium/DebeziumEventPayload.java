package io.github.susimsek.account.debezium;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;

public record DebeziumEventPayload<T extends Object>(
    T before,
    T after,
    Map<String, Object> source,
    @JsonProperty("op") DebeziumEventPayloadOperation operation,
    @JsonProperty("ts_ms") Instant date) {
}