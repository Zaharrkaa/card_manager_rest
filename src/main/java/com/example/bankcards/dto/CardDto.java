package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CardDto {

    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must contain exactly 16 digits")
    private String number;

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    private LocalDate expiryDate;

    @NotBlank(message = "Cvv is required")
    @Size(min = 3, max = 3, message = "Cvv must contain exactly 3 digits")
    private String cvv;

    private Status status;

    @Min(value = 0, message = "Balance can not be negative")
    private Integer balance;

    public CardDto(String number, String ownerName, LocalDate expiryDate, String cvv, Status status, Integer balance) {
        this.number = number;
        this.ownerName = ownerName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.status = status;
        this.balance = balance;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
