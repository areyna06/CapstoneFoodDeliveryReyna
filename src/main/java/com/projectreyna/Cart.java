package com.projectreyna;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<MenuItem> items = new ArrayList<>();

    public void addItem(MenuItem item) { items.add(item); }
    public void removeItem(MenuItem item) { items.remove(item); }
    public void clear() { items.clear(); }
    public List<MenuItem> getItems() { return new ArrayList<>(items); }

    public double getTotal() {
        double total = 0;
        for (MenuItem item : items) total += item.getPrice();
        return total;
    }

    public boolean isEmpty() { return items.isEmpty(); }
}