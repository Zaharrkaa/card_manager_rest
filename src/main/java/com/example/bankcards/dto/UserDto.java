package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;

import java.util.List;

public class UserDto {

    private String username;

    private Role role;

    private List<CardDto> cards;

    public UserDto(String username, Role role, List<CardDto> cards) {
        this.username = username;
        this.role = role;
        this.cards = cards;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<CardDto> getCards() {
        return cards;
    }

    public void setCards(List<CardDto> cards) {
        this.cards = cards;
    }
}
