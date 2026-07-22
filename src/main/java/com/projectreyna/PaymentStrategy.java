package com.projectreyna;

public interface PaymentStrategy {
    boolean pay(double amount);
    String getName();
}