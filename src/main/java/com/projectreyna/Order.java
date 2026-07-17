package com.projectreyna;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int orderId;
    private LocalDateTime dateTime;
    private String status;
    private double total;

    private final Customer customer;
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private Payment payment;
    private Rider rider;

    private int ratingStars;
    private String ratingComment;

    public Order(int orderId, Customer customer, Restaurant restaurant, List<MenuItem> items) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.dateTime = LocalDateTime.now();
        this.status = "Pending";
        this.total = calculateTotal();
    }

    public double calculateTotal() {
        double sum = 0;
        for (MenuItem item : items) sum += item.getPrice();
        return sum;
    }

    public void updateStatus(String newStatus) { this.status = newStatus; }

    public void setRating(int stars, String comment) {
        this.ratingStars = stars;
        this.ratingComment = comment;
    }

    public int getOrderId() { return orderId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getStatus() { return status; }
    public double getTotal() { return total; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public List<MenuItem> getItems() { return items; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public Rider getRider() { return rider; }
    public void setRider(Rider rider) { this.rider = rider; }
    public int getRatingStars() { return ratingStars; }
    public String getRatingComment() { return ratingComment; }
}