package com.projectreyna;

public class CashPayment implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        System.out.println("Cash on delivery: ₱" + amount);
        return true; // COD always accepted at order time
    }

    @Override
    public String getName() {
        return "CASH";
    }
}