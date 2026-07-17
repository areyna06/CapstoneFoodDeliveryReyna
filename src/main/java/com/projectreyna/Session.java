package com.projectreyna;

public class Session {
    public static Customer currentCustomer;
    public static Restaurant currentRestaurant;
    public static Order currentOrder;

    private static int nextOrderId = 1000;
    public static int nextOrderId() { return nextOrderId++; }
}