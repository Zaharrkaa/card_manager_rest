package com.example.bankcards.service;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void save() {
        String username = "testUsername";
        String password = "testPassword";
        AuthDto authDto = new AuthDto(username, password);
        String encodedPassword = "testEncodedPassword";
        String role = Role.ROLE_USER.toString();
        User user = new User(username, encodedPassword, role);
        user.setRole(role);
        when(userMapper.toUser(authDto)).thenReturn(user);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        userService.save(authDto);
        verify(userRepository).save(user);
    }

    @Test
    void delete() {
        String username = "testUsername";
        String password = "testPassword";
        String role = Role.ROLE_USER.toString();
        User user = new User(username, password, role);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        userService.deleteByUsername(user.getUsername());
        verify(userRepository).delete(user);
    }

    @Test
    void findAllWithoutCards() {
    }

    @Test
    void findAllWithCards() {
    }
}
