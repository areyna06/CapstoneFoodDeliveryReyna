package com.projectreyna;

public class CardPayment implements PaymentStrategy {
    private String cardNumber;

    public CardPayment() { }

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Card payment of ₱" + amount);
        return cardNumber != null && cardNumber.length() >= 12;
    }

    @Override
    public String getName() {
        return "CARD";
    }
}