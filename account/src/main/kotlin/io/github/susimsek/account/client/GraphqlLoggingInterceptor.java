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
                logRequest(request, writer);
                logResponse(response, writer);
            } catch (JsonProcessingException ex) {
                log.error(
                    "Error while reading the GraphQL client response || Error Details: {}",
                    ex.getMessage());
            }
        });
    }

    private static void logRequest(ClientGraphQlRequest request,
                                   ObjectWriter writer) throws JsonProcessingException {
        var requestData = request.toMap();
        var requestString = writer.writeValueAsString(requestData);
        if (StringUtils.hasText(requestString)) {
            log.info("Request: %s ".formatted(requestString));
        }
    }

    private void logResponse(
        ClientGraphQlResponse response,
        ObjectWriter writer) throws JsonProcessingException {
        var responseData = response.toMap();
        var responseString = writer
            .writeValueAsString(responseData);
        if (StringUtils.hasText(responseString)) {
            log.info("Response: %s ".formatted(responseString));
        }
    }

    @NotNull
    @Override
    public Flux<ClientGraphQlResponse> interceptSubscription(
        @NotNull ClientGraphQlRequest request,
        SubscriptionChain chain) {
        return chain.next(request);
    }

}