package com.example.bankcards.util;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class CardValidator implements Validator {

    private final CardRepository cardRepository;

    @Autowired
    public CardValidator(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Card.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CardDto cardDto = (CardDto) target;
        Optional<Card> cardOptional = cardRepository.findByNumber(cardDto.getNumber());
        if (cardOptional.isPresent()) {
            errors.rejectValue("number", null, "Card number already exists");
        }
    }
}
