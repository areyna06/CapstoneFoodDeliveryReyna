package com.projectreyna;

public class Payment {
    private int paymentId;
    private String method;
    private double amount;
    private boolean confirmed;

    public Payment(int paymentId, String method, double amount) {
        this.paymentId = paymentId;
        this.method = method;
        this.amount = amount;
    }

    /** Simulates the Payment Gateway (sequence steps 3-4). */
    public boolean process() {
        confirmed = amount > 0;
        return confirmed;
    }

    public int getPaymentId() { return paymentId; }
    public String getMethod() { return method; }
    public double getAmount() { return amount; }
    public boolean isConfirmed() { return confirmed; }
}