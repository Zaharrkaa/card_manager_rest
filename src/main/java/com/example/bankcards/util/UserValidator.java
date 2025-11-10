package com.example.bankcards.util;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AuthDto authDto = (AuthDto) target;
        Optional<User> userOptional = userRepository.findByUsername(authDto.getUsername());
        if (userOptional.isPresent()) {
            errors.rejectValue("username", null, "Username already exists");
        }
    }
}
