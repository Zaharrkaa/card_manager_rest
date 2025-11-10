package com.example.bankcards.util;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "ownerName", expression = "java(card.getOwner().getUsername())")
    CardDto toCardDto(Card card);


    Card toCard(CardDto cardDto);
}
