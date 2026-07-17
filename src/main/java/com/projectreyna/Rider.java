package com.projectreyna;

public class Rider extends User {
    private String vehicleType;
    private String status;

    public Rider(int id, String name, String email, String phone, String password, String vehicleType) {
        super(id, name, email, phone, password);
        this.vehicleType = vehicleType;
        this.status = "Available";
    }

    public void acceptDelivery(Order order) {
        order.setRider(this);
        this.status = "Delivering";
    }

    public void updateStatus(Order order, String newStatus) {
        order.updateStatus(newStatus);
        if (newStatus.equals("Delivered")) this.status = "Available";
    }

    public String getVehicleType() { return vehicleType; }
    public String getStatus() { return status; }
}