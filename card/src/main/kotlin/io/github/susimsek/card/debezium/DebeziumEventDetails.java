package io.github.susimsek.card.debezium;

import java.util.Map;

public record DebeziumEventDetails<T extends Object>(
    Map<String, Object> schema,
    DebeziumEventPayload<T> payload) {
}