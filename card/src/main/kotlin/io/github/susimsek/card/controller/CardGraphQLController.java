package io.github.susimsek.card.controller;

import io.github.susimsek.card.dto.CardDTO;
import io.github.susimsek.card.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

@Slf4j
@GraphQLController
@RequiredArgsConstructor
public class CardGraphQLController {

    private final CardService cardService;

    @QueryMapping
    public CardDTO card(@Argument String mobileNumber) {
        return cardService.getCard(mobileNumber);
    }

    @MutationMapping
    public CardDTO createCard(@Argument String mobileNumber) {
        return  cardService.createCard(mobileNumber);
    }

    @MutationMapping
    public String deleteCard(@Argument String mobileNumber) {
        cardService.deleteCard(mobileNumber);
        return mobileNumber;
    }
}