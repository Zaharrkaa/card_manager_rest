package com.example.bankcards.service;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import com.example.bankcards.exception.CardAccessDeniedException;
import com.example.bankcards.exception.CardNotActiveException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.repository.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransferService {

    private final CardRepository cardRepository;

    public TransferService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public void transfer(TransferDto transferDto, String username) {
        Optional<Card> optionalCardToDebit = cardRepository.findByNumber(transferDto.getNumberOfCardToDebit());
        Optional<Card> optionalCardToReceive = cardRepository.findByNumber(transferDto.getNumberOfCardToReceive());
        if (optionalCardToDebit.isEmpty() || optionalCardToReceive.isEmpty()) {
            throw new CardNotFoundException();
        }
        Card cardToDebit = optionalCardToDebit.get();
        Card cardToReceive = optionalCardToReceive.get();
        if(!cardToDebit.getOwner().getUsername().equals(username) || !cardToReceive.getOwner().getUsername().equals(username)) {
            throw new CardAccessDeniedException();
        }
        cardToDebit.checkExpiryDate();
        cardToReceive.checkExpiryDate();
        if(!cardToDebit.getStatus().equals(Status.ACTIVE) || !cardToReceive.getStatus().equals(Status.ACTIVE)) {
            throw new CardNotActiveException();
        }
        if(cardToDebit.getBalance() < transferDto.getAmount()) {
            throw new InsufficientFundsException();
        }
        cardToDebit.setBalance(cardToDebit.getBalance() - transferDto.getAmount());
        cardToReceive.setBalance(cardToReceive.getBalance() + transferDto.getAmount());
    }
}
