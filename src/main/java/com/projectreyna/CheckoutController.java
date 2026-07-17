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
        paymentMessageLabel.setText("");

        if (Session.currentCustomer == null) {
            showError("No customer is currently logged in.");
            return;
        }

        if (Session.currentRestaurant == null) {
            showError("No restaurant has been selected.");
            return;
        }

        if (cart == null || cart.getItems().isEmpty()) {
            showError("Your cart is empty.");
            return;
        }

        String paymentMethod = paymentMethodBox.getValue();

        if (paymentMethod == null || paymentMethod.isBlank()) {
            showError("Please select a payment method.");
            return;
        }

        try {
            Order order = Session.currentCustomer.placeOrder(
                    Session.nextOrderId(),
                    Session.currentRestaurant
            );

            if (order == null) {
                showError("Unable to create the order.");
                return;
            }

            Payment payment = new Payment(
                    order.getOrderId(),
                    paymentMethod,
                    order.getTotal()
            );

            if (!payment.process()) {
                showError("Payment failed. Try another payment method.");
                return;
            }

            order.setPayment(payment);
            order.updateStatus("Confirmed");

            /*
             * This must be assigned before loading tracking-view.fxml.
             * TrackingController reads Session.currentOrder.
             */
            Session.currentOrder = order;

            showSuccess("Order placed successfully.");

            SceneSwitcher.switchTo(
                    "tracking-view.fxml",
                    totalLabel
            );

        } catch (Exception e) {
            showError("Unable to place the order: " + e.getMessage());
            e.printStackTrace();
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