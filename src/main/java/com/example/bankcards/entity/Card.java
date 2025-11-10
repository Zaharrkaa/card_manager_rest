package com.example.bankcards.entity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MM/yyyy")
    private LocalDate expiryDate;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "balance")
    private int balance;

    public Card() {

    }

    public Card(String number, LocalDate expiryDate, String cvv, Status status, int balance) {
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.status = status;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void checkExpiryDate(){
        if(this.expiryDate.isBefore(LocalDate.now())){
            this.setStatus(Status.EXPIRED);
        }
    }
}
