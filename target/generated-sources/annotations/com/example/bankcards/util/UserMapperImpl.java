package com.example.bankcards.util;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T13:13:39+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDtoWithoutCards(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;
        Role role = null;

        username = user.getUsername();
        if ( user.getRole() != null ) {
            role = Enum.valueOf( Role.class, user.getRole() );
        }

        List<CardDto> cards = null;

        UserDto userDto = new UserDto( username, role, cards );

        return userDto;
    }

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;
        Role role = null;
        List<CardDto> cards = null;

        username = user.getUsername();
        if ( user.getRole() != null ) {
            role = Enum.valueOf( Role.class, user.getRole() );
        }
        cards = cardListToCardDtoList( user.getCards() );

        UserDto userDto = new UserDto( username, role, cards );

        return userDto;
    }

    @Override
    public User toUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( userDto.getUsername() );
        if ( userDto.getRole() != null ) {
            user.setRole( userDto.getRole().name() );
        }
        user.setCards( cardDtoListToCardList( userDto.getCards() ) );

        return user;
    }

    @Override
    public User toUser(AuthDto authDto) {
        if ( authDto == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( authDto.getPassword() );
        user.setUsername( authDto.getUsername() );

        return user;
    }

    protected CardDto cardToCardDto(Card card) {
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

        String ownerName = null;

        CardDto cardDto = new CardDto( number, ownerName, expiryDate, cvv, status, balance );

        return cardDto;
    }

    protected List<CardDto> cardListToCardDtoList(List<Card> list) {
        if ( list == null ) {
            return null;
        }

        List<CardDto> list1 = new ArrayList<CardDto>( list.size() );
        for ( Card card : list ) {
            list1.add( cardToCardDto( card ) );
        }

        return list1;
    }

    protected Card cardDtoToCard(CardDto cardDto) {
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

    protected List<Card> cardDtoListToCardList(List<CardDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Card> list1 = new ArrayList<Card>( list.size() );
        for ( CardDto cardDto : list ) {
            list1.add( cardDtoToCard( cardDto ) );
        }

        return list1;
    }
}
