package io.github.susimsek.card.service.impl;

import io.github.susimsek.card.constants.CardConstants;
import io.github.susimsek.card.dto.CardDTO;
import io.github.susimsek.card.entity.Card;
import io.github.susimsek.card.exception.CardAlreadyExistsException;
import io.github.susimsek.card.exception.ResourceNotFoundException;
import io.github.susimsek.card.mapper.CardMapper;
import io.github.susimsek.card.repository.CardRepository;
import io.github.susimsek.card.service.CardService;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    @Override
    public void createCard(String mobileNumber) {
        if (cardRepository.existsByMobileNumber(mobileNumber)) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }
        cardRepository.save(createNewCard(mobileNumber));
    }

    @Override
    public CardDTO fetchCard(String mobileNumber) {
        var card = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
            () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        return cardMapper.toDto(card);
    }

    @Override
    public boolean updateCard(String cardNumber, CardDTO card) {
        var cardEntity = cardRepository.findByCardNumber(cardNumber).orElseThrow(
            () -> new ResourceNotFoundException("Card", "cardNumber", cardNumber)
        );
        cardMapper.partialUpdate(cardEntity, card);
        cardEntity.setCardNumber(cardNumber);
        cardRepository.save(cardEntity);
        return true;
    }

    @Override
    public void deleteCard(String mobileNumber) {
        var card = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
            () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardRepository.deleteById(card.getCardId());
    }

    private Card createNewCard(String mobileNumber) {
        Card newCard = new Card();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardConstants.NEW_CARD_LIMIT);
        return newCard;
    }
}
