package com.projectreyna;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class CheckoutController {

    @FXML
    private ListView<MenuItem> orderItemsList;

    @FXML
    private Label totalLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private ComboBox<String> paymentMethodBox;

    @FXML
    private Label paymentMessageLabel;

    private Cart cart;

    @FXML
    public void initialize() {
        paymentMessageLabel.setText("");

        if (Session.currentCustomer == null) {
            showError("No customer is currently logged in.");
            disableCheckout();
            return;
        }

        cart = Session.currentCustomer.getCart();

        if (cart == null) {
            showError("Your cart could not be loaded.");
            disableCheckout();
            return;
        }

        orderItemsList.getItems().setAll(cart.getItems());

        totalLabel.setText(
                String.format("Total: ₱%.2f", cart.getTotal())
        );

        String address = Session.currentCustomer.getAddress();

        if (address == null || address.isBlank()) {
            addressLabel.setText("Deliver to: No address provided");
        } else {
            addressLabel.setText("Deliver to: " + address);
        }

        paymentMethodBox.getItems().setAll(
                "Cash on Delivery",
                "GCash",
                "Credit Card"
        );

        paymentMethodBox.setValue("Cash on Delivery");
    }

    @FXML
    private void handlePlaceOrder() {
        // guard: cart must have items
        if (cart == null || cart.getItems().isEmpty()) {
            showError("Your cart is empty.");
            return;
        }

        // 1. pick the strategy from the ComboBox selection
        String method = paymentMethodBox.getValue();
        PaymentStrategy strategy;
        if ("GCash".equals(method)) {
            strategy = new GcashPayment(Session.currentCustomer.getPhone());
        } else if ("Credit Card".equals(method)) {
            strategy = new CardPayment("0000000000000000"); // placeholder card no.
        } else {
            strategy = new CashPayment();
        }

        // 2. place the order through the Facade
        Order order = new OrderService().placeOrder(
                Session.nextOrderId(),          // real unique ID from Session
                Session.currentCustomer,
                Session.currentRestaurant,
                cart.getItems(),
                strategy
        );

        // 4. react to the result
        if (order.getStatus().equals("CONFIRMED")) {
            Session.currentOrder = order;               // <-- ADD THIS LINE
            cart.clear();
            showSuccess("Order confirmed! Total: ₱" + String.format("%.2f", order.getTotal()));
            SceneSwitcher.switchTo("tracking-view.fxml", totalLabel);
        } else {
            showError("Payment failed. Please try again.");
        }
    }

    @FXML
    private void handleBackToMenu() {
        SceneSwitcher.switchTo(
                "menu-view.fxml",
                totalLabel
        );
    }

    private void showError(String message) {
        paymentMessageLabel.setStyle("-fx-text-fill: red;");
        paymentMessageLabel.setText(message);
    }

    private void showSuccess(String message) {
        paymentMessageLabel.setStyle("-fx-text-fill: green;");
        paymentMessageLabel.setText(message);
    }

    private void disableCheckout() {
        orderItemsList.setDisable(true);
        paymentMethodBox.setDisable(true);
    }
}