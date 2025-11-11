package com.example.bankcards.service;


import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CardService cardService;

    @Test
    void save(){
        User user = new User("test", "pass", "ROLE_USER");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        String number = "1234567812345678";
        LocalDate expiryDate = LocalDate.now().plusYears(3);
        String cvv = "133";
        Status status = Status.ACTIVE;
        int balance = 0;
        CardDto cardDto = new CardDto(number, user.getUsername(), expiryDate, cvv, status, balance);
        Card card = new Card(number, expiryDate, cvv, status, balance);
        when(cardMapper.toCard(cardDto)).thenReturn(card);
        CardDto testCardDto = cardService.save(cardDto);
        Assertions.assertEquals(cardDto, testCardDto);
    }

    @Test
    void delete(){
        String number = "1234567812345678";
        LocalDate expiryDate = LocalDate.now().plusYears(3);
        String cvv = "133";
        Status status = Status.ACTIVE;
        int balance = 0;
        Card card = new Card(number, expiryDate, cvv, status, balance);
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));
        Optional<Card> testCard = cardRepository.findByNumber(card.getNumber());
        cardService.delete(testCard.get().getNumber());
        verify(cardRepository).delete(card);
    }

    @Test
    void block(){
        String number = "1234567812345678";
        LocalDate expiryDate = LocalDate.now().plusYears(3);
        String cvv = "133";
        Status status = Status.ACTIVE;
        int balance = 0;
        Card card = new Card(number, expiryDate, cvv, status, balance);
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));
        Optional<Card> testCard = cardRepository.findByNumber(card.getNumber());
        cardService.block(testCard.get().getNumber());
        Assertions.assertEquals(Status.BLOCKED, testCard.get().getStatus());
    }

    @Test
    void activate(){
        String number = "1234567812345678";
        LocalDate expiryDate = LocalDate.now().plusYears(3);
        String cvv = "133";
        Status status = Status.BLOCKED;
        int balance = 0;
        Card card = new Card(number, expiryDate, cvv, status, balance);
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));
        Optional<Card> testCard = cardRepository.findByNumber(card.getNumber());
        cardService.activate(testCard.get().getNumber());
        Assertions.assertEquals(Status.ACTIVE, testCard.get().getStatus());
    }

    @Test
    void balance() {
        User user = new User("test", "pass", "ROLE_USER");
        String number = "1234567812345678";
        LocalDate expiryDate = LocalDate.now().plusYears(3);
        String cvv = "133";
        Status status = Status.ACTIVE;
        int balance = 100;
        Card card = new Card(number, expiryDate, cvv, status, balance);
        card.setOwner(user);
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));
        int testBalance = cardService.balance(card.getNumber(), user.getUsername());
        Assertions.assertEquals(testBalance, card.getBalance());

    }

    @Test
    void findAll(){
        LocalDate expiryDate = LocalDate.now().plusYears(3);
        String cvv = "133";
        Status status = Status.ACTIVE;
        int balance = 100;

        Card card1 = new Card("1234567812345678", expiryDate, cvv, status, balance);
        Card card2 = new Card("1234567812345677", expiryDate, cvv+1, status, balance+1);
        Card card3 = new Card("1234567812345676", expiryDate, cvv+2, status, balance+2);
        when(cardRepository.findAll()).thenReturn(List.of(card1, card2, card3));

        CardDto cardDto1 = new CardDto("1234567812345678", "testUser1", expiryDate, cvv, status, balance);
        CardDto cardDto2 = new CardDto("1234567812345677", "testUser2", expiryDate, cvv+1, status, balance+1);
        CardDto cardDto3 = new CardDto("1234567812345676", "testUser3", expiryDate, cvv+2, status, balance+2);
        List<CardDto> cardDtos = List.of(cardDto1, cardDto2, cardDto3);
        when(cardMapper.toCardDto(card1)).thenReturn(cardDto1);
        when(cardMapper.toCardDto(card2)).thenReturn(cardDto2);
        when(cardMapper.toCardDto(card3)).thenReturn(cardDto3);

        List<CardDto> testCardDtos = cardService.findAll();
        Assertions.assertEquals(testCardDtos, cardDtos);

    }
}
