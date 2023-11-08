package io.github.susimsek.card.mapper;

import io.github.susimsek.card.entity.Card;
import io.github.susimsek.card.dto.CardDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDTO, Card> {
}
