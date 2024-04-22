package io.github.susimsek.card.debezium;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DebeziumEventPayloadOperation {
    CREATE("c"),
    UPDATE("u"),
    DELETE("r");

    @Getter(onMethod_ = @JsonValue)
    private final String code;
}