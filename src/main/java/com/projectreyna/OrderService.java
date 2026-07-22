package com.projectreyna;

import java.util.List;

public class OrderService {

    // FACADE: hides the order-building → payment → finalize steps behind one call
    public Order placeOrder(int orderId,
                            Customer customer,
                            Restaurant restaurant,
                            List<MenuItem> items,
                            PaymentStrategy paymentStrategy) {

        // 1. build the order (your Order sets total + status in its constructor)
        Order order = new Order(orderId, customer, restaurant, items);

        // 2. process payment via the chosen strategy
        boolean paid = paymentStrategy.pay(order.getTotal());

        if (!paid) {
            order.updateStatus("PAYMENT_FAILED");
            return order;
        }

        // 3. record the payment and confirm
        Payment payment = new Payment(orderId, paymentStrategy.getName(), order.getTotal());
        payment.process();                 // your Payment's own gateway check
        order.setPayment(payment);
        order.updateStatus("CONFIRMED");
        return order;
    }
}