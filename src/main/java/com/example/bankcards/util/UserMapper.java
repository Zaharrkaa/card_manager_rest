package com.example.bankcards.util;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "cards", ignore = true)
    UserDto toUserDtoWithoutCards(User user);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    User toUser(AuthDto authDto);
}
