package io.github.susimsek.card.service;

import io.github.susimsek.card.dto.CardDTO;

public interface CardService {

    CardDTO createCard(String mobileNumber);

    CardDTO getCard(String mobileNumber);

    boolean updateCard(String cardNumber, CardDTO card);

    void deleteCard(String mobileNumber);
}
