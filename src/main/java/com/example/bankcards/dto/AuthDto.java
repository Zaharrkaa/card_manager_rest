package com.example.bankcards.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username should be at least 3 symbols")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 symbols")
    private String password;

    public AuthDto(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
