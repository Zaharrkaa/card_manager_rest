package com.example.bankcards.service;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransferService transferService;


    @Test
    void transfer() {
        TransferDto transferDto = new TransferDto("1111222233334444", "5555666677778888", 100);
        User owner = new User("test_user", "password", "ROLE_USER");
        Card cardToDebit = new Card("1111222233334444", LocalDate.now().plusYears(3), "455", Status.ACTIVE, 400);
        cardToDebit.setOwner(owner);
        Card cardToReceive = new Card("5555666677778888", LocalDate.now().plusYears(3), "455", Status.ACTIVE, 200);
        cardToReceive.setOwner(owner);
        when(cardRepository.findByNumber("1111222233334444")).thenReturn(Optional.of(cardToDebit));
        when(cardRepository.findByNumber("5555666677778888")).thenReturn(Optional.of(cardToReceive));
        transferService.transfer(transferDto, "test_user");
        assertEquals(300, cardToDebit.getBalance());
        assertEquals(300, cardToReceive.getBalance());
        verify(cardRepository).findByNumber("1111222233334444");
        verify(cardRepository).findByNumber("5555666677778888");
    }
}