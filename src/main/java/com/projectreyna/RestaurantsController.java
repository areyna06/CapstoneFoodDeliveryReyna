package com.projectreyna;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class RestaurantsController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Restaurant> restaurantList;

    /*
     * Dependency Inversion Principle (DIP):
     * The controller depends on the RestaurantRepository
     * abstraction, not on a concrete database class.
     */
    private final RestaurantRepository restaurantRepository =
            new MySqlRestaurantRepository();

    @FXML
    public void initialize() {
        /*
         * Session validation: this screen reads the serialized
         * session.dat file. If it does not exist, the user is
         * sent back to the login screen.
         */
        SessionData session = SessionManager.getSession();

        if (session == null) {
            Platform.runLater(() ->
                    SceneSwitcher.switchTo("login-view.fxml", restaurantList));
            return;
        }

        welcomeLabel.setText("Welcome, " + session.getName() + "!");

        List<Restaurant> restaurants = restaurantRepository.findAll();

        ObservableList<Restaurant> items =
                FXCollections.observableArrayList(restaurants);
        FilteredList<Restaurant> filtered =
                new FilteredList<>(items, restaurant -> true);

        restaurantList.setItems(filtered);

        searchField.textProperty().addListener((obs, oldText, newText) ->
                filtered.setPredicate(restaurant ->
                        newText == null
                                || newText.isBlank()
                                || restaurant.getName().toLowerCase()
                                        .contains(newText.toLowerCase())));
    }

    @FXML
    private void handleViewMenu() {
        Restaurant selected =
                restaurantList.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please select a restaurant first.").showAndWait();
            return;
        }

        Session.currentRestaurant = selected;

        if (getClass().getResource("menu-view.fxml") == null) {
            new Alert(Alert.AlertType.INFORMATION,
                    "The menu screen is not available yet.").showAndWait();
            return;
        }

        SceneSwitcher.switchTo("menu-view.fxml", restaurantList);
    }

    @FXML
    private void handleLogout() {
        /*
         * Logout: session.dat is deleted automatically and the
         * user is redirected to the login screen.
         */
        LoginController.clearLoginSession();
        SceneSwitcher.switchTo("login-view.fxml", restaurantList);
    }
}
