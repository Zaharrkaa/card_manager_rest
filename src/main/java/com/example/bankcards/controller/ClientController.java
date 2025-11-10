package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.exception.CardAccessDeniedException;
import com.example.bankcards.exception.CardNotActiveException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@Tag(name = "Client controller", description = "Контроллер для операций клиента")
public class ClientController {

    private final CardService cardService;
    private final TransferService transferService;

    @Autowired
    public ClientController(CardService cardService, TransferService transferService) {
        this.cardService = cardService;
        this.transferService = transferService;
    }

    @GetMapping("/cards")
    @Operation(summary = "Возвращает список карт пользователя")
    public ResponseEntity<Page<CardDto>> getClientCards(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestParam Integer page,
                                                        @RequestParam Integer size,
                                                        @RequestParam String search) {
        return ResponseEntity.ok(cardService.findByNumberLike(page, size, search, userDetails.getUsername()));
    }

    @GetMapping("/balance/{number}")
    @Operation(summary = "Возвращает баланс карты", description = "Возвращает баланс карты, номер которой указан в URL (только карты клиента)")
    public ResponseEntity<Integer> balance(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String number) {
        return ResponseEntity.ok(cardService.balance(number, userDetails.getUsername()));
    }

    @PatchMapping("/transfer")
    @Operation(summary = "Совершает перевод средств с одной карты на другую", description = "Принимает данные о переводе, совершает перевод если обе карты активны, являются картами пользователя и на балансе достаточно средст")
    public ResponseEntity<String> transfer(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TransferDto transferDto) {
        transferService.transfer(transferDto, userDetails.getUsername());
        return ResponseEntity.ok("Transfer successful");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(CardAccessDeniedException e) {
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied for this card");
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(CardNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(CardNotActiveException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card not active");
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(InsufficientFundsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("insufficient funds on the card");
    }

}
