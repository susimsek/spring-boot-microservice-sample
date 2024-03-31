package io.github.susimsek.account.client;

import io.github.susimsek.account.dto.CardDTO;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

@Component
public class CardGraphQlClient {

    private final HttpGraphQlClient client;

    public CardGraphQlClient(HttpGraphQlClient.Builder<?> builder) {
        client = builder.url("http://card/graphql")
            .build();
    }

    public CardDTO fetchCardDetails(String mobileNumber) {
        return client.documentName("getCardDetails")
            .variable("mobileNumber", "4354437681")
            .retrieve("card")
            .toEntity(CardDTO.class)  // possibly also generated or imported if available
            .block();
    }
}
