package com.projectreyna;

public class Customer extends User {
    private String address;
    private Cart cart;

    public Customer(int id, String name, String email, String phone, String password, String address) {
        super(id, name, email, phone, password);
        this.address = address;
        this.cart = new Cart();
    }

    public Order placeOrder(int orderId, Restaurant restaurant) {
        Order order = new Order(orderId, this, restaurant, cart.getItems());
        cart.clear();
        return order;
    }

    public String trackOrder(Order order) { return order.getStatus(); }

    public void rateOrder(Order order, int stars, String comment) {
        order.setRating(stars, comment);
    }

    public String getAddress() { return address; }
    public Cart getCart() { return cart; }
}