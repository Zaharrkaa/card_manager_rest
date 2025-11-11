package com.example.bankcards.util;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-11T12:50:09+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public CardDto toCardDto(Card card) {
        if ( card == null ) {
            return null;
        }

        Integer balance = null;
        String cvv = null;
        LocalDate expiryDate = null;
        String number = null;
        Status status = null;

        balance = card.getBalance();
        cvv = card.getCvv();
        expiryDate = card.getExpiryDate();
        number = card.getNumber();
        status = card.getStatus();

        String ownerName = card.getOwner().getUsername();

        CardDto cardDto = new CardDto( number, ownerName, expiryDate, cvv, status, balance );

        return cardDto;
    }

    @Override
    public Card toCard(CardDto cardDto) {
        if ( cardDto == null ) {
            return null;
        }

        Card card = new Card();

        if ( cardDto.getBalance() != null ) {
            card.setBalance( cardDto.getBalance() );
        }
        card.setStatus( cardDto.getStatus() );
        card.setCvv( cardDto.getCvv() );
        card.setExpiryDate( cardDto.getExpiryDate() );
        card.setNumber( cardDto.getNumber() );

        return card;
    }
}
