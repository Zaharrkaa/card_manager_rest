package com.example.bankcards.service;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Класс с реализацией бизнес логики связанной с пользователями
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public void save(AuthDto authDto) {
        User user = userMapper.toUser(authDto);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER.toString());
        userRepository.save(user);
    }


    @Transactional
    public void deleteByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }
        else {
            throw new UserNotFoundException();
        }
    }

    public List<UserDto> findAllWithoutCards() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toUserDtoWithoutCards).toList();
    }

    public List<UserDto> findAllWithCards() {
        List<User> users = userRepository.findAllWithCards();
        return users.stream().map(userMapper::toUserDto).toList();
    }
}
