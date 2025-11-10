package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cards")
@Tag(name = "Card controller", description = "Контроллер для управления картами (доступен только админу)")
public class CardController {
    private final CardService cardService;
    private final CardValidator cardValidator;

    @Autowired
    public CardController(CardService cardService, CardValidator cardValidator) {
        this.cardService = cardService;
        this.cardValidator = cardValidator;
    }

    @GetMapping()
    public ResponseEntity<List<CardDto>> getAllCards() {
        return ResponseEntity.ok(cardService.findAll());
    }

    @PostMapping("/create")
    @Operation(summary = "Создаёт карту", description = "Принимает dto, валидирует, конвертирует в сущность и сохраняет в базе данных")
    public ResponseEntity<String> createCard(@RequestBody @Valid CardDto cardDto, BindingResult bindingResult) {
        cardValidator.validate(cardDto, bindingResult);
        if (!bindingResult.hasErrors()) {
            cardService.save(cardDto);
            return ResponseEntity.ok("Card created successfully");
        }
        StringBuilder stringBuilder = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> stringBuilder.append(error.getDefaultMessage()).append("\n"));
        String errorMessage = stringBuilder.toString();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @PatchMapping("/activate/{cardNumber}")
    @Operation(summary = "Активирует карту", description = "Меняет статус на \"ACTIVE\" у карты, номер которой указан в URL")
    public ResponseEntity<String> activateCard(@PathVariable String cardNumber) {
        cardService.activate(cardNumber);
        return ResponseEntity.ok("Card activated successfully");
    }
    @PatchMapping("/block/{cardNumber}")
    @Operation(summary = "Блокирует карту", description = "Меняет статус на \"BLOCKED\" у карты, номер которой указан в URL")
    public ResponseEntity<String> blockCard(@PathVariable String cardNumber) {
        cardService.block(cardNumber);
        return ResponseEntity.ok("Card blocked successfully");
    }

    @DeleteMapping("/delete/{number}")
    @Operation(summary = "Удаляет карту", description = "Удаляет карту, нномер которой указан в URL")
    public ResponseEntity<String> deleteCard(@PathVariable String number) {
        cardService.delete(number);
        return ResponseEntity.ok("Card deleted successfully");
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(CardNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
    }
}
