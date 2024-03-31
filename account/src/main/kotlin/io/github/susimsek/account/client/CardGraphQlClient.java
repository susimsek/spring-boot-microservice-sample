package io.github.susimsek.account.client;

import io.github.susimsek.account.dto.CardDTO;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CardGraphQlClient {

    private final HttpGraphQlClient client;

    public CardGraphQlClient(WebClient.Builder loadBalancedWebClientBuilder) {
        var webClient = loadBalancedWebClientBuilder
            .baseUrl("http://card/graphql")
            .build();
        client = HttpGraphQlClient.create(webClient);
    }

    public CardDTO fetchCardDetails(String mobileNumber) {
        return client.documentName("getCardDetails")
            .variable("mobileNumber", mobileNumber)
            .retrieve("card")
            .toEntity(CardDTO.class)  // possibly also generated or imported if available
            .block();
    }
}
