package com.projectreyna;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private String location;
    private final List<MenuItem> menu = new ArrayList<>();

    public Restaurant(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public void manageMenu(MenuItem item) { menu.add(item); }
    public void acceptOrder(Order order) { order.updateStatus("Accepted"); }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public List<MenuItem> getMenu() { return menu; }

    @Override
    public String toString() { return name + " — " + location; }
}