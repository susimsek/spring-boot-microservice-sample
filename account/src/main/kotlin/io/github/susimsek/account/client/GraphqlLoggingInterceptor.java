package io.github.susimsek.account.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.client.ClientGraphQlRequest;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.GraphQlClientInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GraphqlLoggingInterceptor implements GraphQlClientInterceptor {

    private final ObjectMapper om;

    @NotNull
    @Override
    public Mono<ClientGraphQlResponse> intercept(
        @NotNull ClientGraphQlRequest request,
        Chain chain) {
        var writer = om.writerWithDefaultPrettyPrinter();
        return chain.next(request).doOnNext(response -> {
            try {
                logRequest("Request", request, writer);
                logResponse("Response", response, writer);
            } catch (JsonProcessingException ex) {
                log.error(
                    "Error while reading the GraphQL client response || Error Details: {}",
                    ex.getMessage());
            }
        });
    }

    private void logRequest(
        String text,
        ClientGraphQlRequest request,
                                   ObjectWriter writer) throws JsonProcessingException {
        var requestData = request.toMap();
        var requestString = writer.writeValueAsString(requestData);
        if (StringUtils.hasText(requestString)) {
            logPayload(text, requestString);
        }
    }

    private void logResponse(
        String text,
        ClientGraphQlResponse response,
        ObjectWriter writer) throws JsonProcessingException {
        var responseData = response.toMap();
        var responseString = writer
            .writeValueAsString(responseData);
        if (StringUtils.hasText(responseString)) {
            logPayload(text, responseString);
        }
    }

    private void logPayload(
        String text,
        String payload) {
        if (StringUtils.hasText(payload)) {
            log.info("%s: %s ".formatted(text, payload));
        }
    }

    @NotNull
    @Override
    public Flux<ClientGraphQlResponse> interceptSubscription(
        @NotNull ClientGraphQlRequest request,
        SubscriptionChain chain) {
        var writer = om.writerWithDefaultPrettyPrinter();
        return chain.next(request).doOnNext(response -> {
            try {
                logRequest("Subscription request", request, writer);
                logResponse("Subscription response", response, writer);
            } catch (JsonProcessingException ex) {
                log.error(
                    "Error while reading the GraphQL subscription client response || Error Details: {}",
                    ex.getMessage());
            }
        });
    }

}