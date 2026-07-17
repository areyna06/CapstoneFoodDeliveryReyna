package com.projectreyna;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class RatingController {
    @FXML private ComboBox<Integer> starsBox;
    @FXML private TextArea commentArea;

    @FXML
    public void initialize() {
        starsBox.getItems().addAll(1, 2, 3, 4, 5);
        starsBox.setValue(5);
    }

    @FXML
    private void handleSubmitRating() {
        Session.currentCustomer.rateOrder(
                Session.currentOrder, starsBox.getValue(), commentArea.getText());

        new Alert(Alert.AlertType.INFORMATION,
                "Thanks! You rated Order #" + Session.currentOrder.getOrderId()
                        + " " + starsBox.getValue() + " star(s).").showAndWait();

        Session.currentOrder = null;
        SceneSwitcher.switchTo("restaurants-view.fxml", starsBox);
    }
}