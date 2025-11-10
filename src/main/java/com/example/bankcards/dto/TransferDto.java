package com.example.bankcards.dto;

public class TransferDto {

    private String numberOfCardToDebit;

    private String numberOfCardToReceive;

    private Integer amount;

    public TransferDto(String numberOfCardToDebit, String numberOfCardToReceive, Integer amount) {
        this.numberOfCardToDebit = numberOfCardToDebit;
        this.numberOfCardToReceive = numberOfCardToReceive;
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNumberOfCardToDebit() {
        return numberOfCardToDebit;
    }

    public void setNumberOfCardToDebit(String numberOfCardToDebit) {
        this.numberOfCardToDebit = numberOfCardToDebit;
    }

    public String getNumberOfCardToReceive() {
        return numberOfCardToReceive;
    }

    public void setNumberOfCardToReceive(String numberOfCardToReceive) {
        this.numberOfCardToReceive = numberOfCardToReceive;
    }
}
