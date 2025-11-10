package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.security.JWTService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер с эндпоинтами для аутентификации и регистрации
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication controller", description = "Контроллер для регистрации пользователей и выдачи токенов")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JWTService jwtService, UserValidator userValidator) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userValidator = userValidator;
    }

    /**
     *
     * @param authDto Данные для аутентификации
     * @param bindingResult Контейнер для ошибок
     * @return HTTP ответ с ответом об успешной регистрации либо ошибкой
     */
    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя", description = "Получает dto, валидирует, конвертирует в сущность и сохраняет в базе данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация пользователя")
    })
    public ResponseEntity<String> registerUser(@RequestBody @Valid AuthDto authDto, BindingResult bindingResult) {
        userValidator.validate(authDto, bindingResult);
        if (!bindingResult.hasErrors()) {
            userService.save(authDto);
            return ResponseEntity.ok("Successfully registered!");
        }
        StringBuilder stringBuilder = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> stringBuilder.append(error.getDefaultMessage()).append("\n"));
        String errorMessage = stringBuilder.toString();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @PostMapping("/token")
    @Operation(summary = "Выдача токена", description = "Аутентифицирует пользователья и выдаёт токен")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "успешная выдача токена")
    })
    public ResponseEntity<String> loginUser(@RequestBody AuthDto authdto) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authdto.getUsername(), authdto.getPassword()));
            String token = jwtService.generateToken(authdto.getUsername());
            return ResponseEntity.ok(token);
        }
        catch(AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad Credentials");
        }
    }
}
