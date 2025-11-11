package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер с эндпоинтами для управления пользователями
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User controller", description = "Контроллер для управления пользователями (доступен только админу)")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @Operation(summary = "Создаёт пользователя", description = "Принимает dto, валидирует, конвертирует в сущность и сохраняет в базе данных")
    public ResponseEntity<String> createUser(@RequestBody AuthDto authDto) {
        userService.save(authDto);
        return ResponseEntity.ok("User created successfully");
    }

    @DeleteMapping("/delete/{username}")
    @Operation(summary = "Удаляет пользователя", description = "Удаляет пользователя с именем, указанным в URL")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/all")
    @Operation(summary = "Возвращает список всех пользователей", description = "Возвращает список пользователей со связными сущностями или без них")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(defaultValue = "false") String withCards) {
        if (withCards.equals("true")) {
            return ResponseEntity.ok(userService.findAllWithCards());
        }
        return ResponseEntity.ok(userService.findAllWithoutCards());
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

}
