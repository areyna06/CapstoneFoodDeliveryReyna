package com.projectreyna;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuController {

    @FXML private Label restaurantNameLabel;
    @FXML private ListView<MenuItem> menuList;   // YOUR MenuItem
    @FXML private Label cartLabel;

    @FXML
    public void initialize() {
        if (Session.currentRestaurant == null) {
            new Alert(Alert.AlertType.ERROR, "No restaurant selected.").showAndWait();
            return;
        }
        restaurantNameLabel.setText(Session.currentRestaurant.getName());
        loadMenuFromDB();
        refreshCartLabel();
    }

    private void loadMenuFromDB() {
        String sql = "SELECT id, name, price FROM menu_items WHERE restaurant_id = ? ORDER BY name";

        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Session.currentRestaurant.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                menuList.getItems().clear();
                while (resultSet.next()) {
                    menuList.getItems().add(new MenuItem(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load menu: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleAddToCart() {
        MenuItem selected = menuList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an item first.").showAndWait();
            return;
        }
        Session.currentCustomer.getCart().addItem(selected); // <-- verify method name (see below)
        refreshCartLabel();
    }

    private void refreshCartLabel() {
        Cart cart = Session.currentCustomer.getCart();
        int count = cart.getItems().size();
        double total = cart.getTotal();
        cartLabel.setText(String.format("Cart: %d item(s) — ₱%.2f", count, total));
    }

    @FXML
    private void handleCheckout() {
        Cart cart = Session.currentCustomer.getCart();
        if (cart.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Your cart is empty.").showAndWait();
            return;
        }
        SceneSwitcher.switchTo("checkout-view.fxml", menuList);
    }

    @FXML
    private void handleBack() {
        SceneSwitcher.switchTo("restaurants-view.fxml", menuList);
    }
}