package com.projectreyna;

public class GcashPayment implements PaymentStrategy {
    private String gcashNumber;

    public GcashPayment() { }

    public GcashPayment(String gcashNumber) {
        this.gcashNumber = gcashNumber;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("GCash payment of ₱" + amount + " from " + gcashNumber);
        return amount > 0; // stub: replace with real gateway check
    }

    @Override
    public String getName() {
        return "GCASH";
    }
}