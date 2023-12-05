package io.github.susimsek.card.service;

import io.github.susimsek.card.dto.CardDTO;

public interface CardService {

    void createCard(String mobileNumber);

    CardDTO fetchCard(String mobileNumber);

    boolean updateCard(String cardNumber, CardDTO card);

    void deleteCard(String mobileNumber);
}
