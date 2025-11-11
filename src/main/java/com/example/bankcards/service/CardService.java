package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardAccessDeniedException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserRepository userRepository;

    @Autowired
    public CardService(CardRepository cardRepository, CardMapper cardMapper, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public CardDto save(CardDto cardDto) {
        Optional<User> user = userRepository.findByUsername(cardDto.getOwnerName());
        if (user.isPresent()) {
            Card card = cardMapper.toCard(cardDto);
            card.setOwner(user.get());
            cardRepository.save(card);
            return cardDto;
        }
        else throw new UserNotFoundException();
    }

    @Transactional
    public void delete(String number){
        Optional<Card> card = cardRepository.findByNumber(number);
        if (card.isPresent()) {
            cardRepository.delete(card.get());
        }
        else throw new CardNotFoundException();
    }

    @Transactional
    public void activate(String number) {
        Optional<Card> card = cardRepository.findByNumber(number);
        if (card.isPresent()) {
            Card cardToActivate = card.get();
            cardToActivate.setStatus(Status.ACTIVE);
        }
        else throw new CardNotFoundException();
    }

    @Transactional
    public void block(String number) {
        Optional<Card> card = cardRepository.findByNumber(number);
        if (card.isPresent()) {
            Card cardToActivate = card.get();
            cardToActivate.setStatus(Status.BLOCKED);
        }
        else throw new CardNotFoundException();
    }

    /**
     * поиск карт по заданному значению
     * @param page
     * @param size
     * @param number
     * @param ownerName
     * @return Страница с заданным количеством объектов а так же информацию о других страницах
     */
    public Page<CardDto> findByNumberLike(int page, int size, String number, String ownerName) {
        Optional<User> user = userRepository.findByUsername(ownerName);
        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }
        Page<Card> cards = cardRepository.findByOwnerAndNumberContaining(PageRequest.of(page, size), user.get(), number);
        return cards.map(cardMapper::toCardDto);
    }


    public Integer balance(String number, String ownerName) {
        Optional<Card> card = cardRepository.findByNumber(number);
        if (!card.isPresent()) {
            throw new CardNotFoundException();
        }
        Card cardToCheck = card.get();
        if(!cardToCheck.getOwner().getUsername().equals(ownerName)){
            throw new CardAccessDeniedException();
        }
        return cardToCheck.getBalance();
    }

    public List<CardDto> findAll(){
        List<Card> cards = cardRepository.findAll();
        return cards.stream().map(cardMapper::toCardDto).toList();
    }
}
