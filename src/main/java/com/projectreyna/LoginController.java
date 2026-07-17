package com.projectreyna;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    private static final String REGISTER_PAGE = "register-view.fxml";
    private static final String CUSTOMER_PAGE = "restaurants-view.fxml";
    private static final String RIDER_PAGE = "rider-view.fxml";

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private static int loggedInUserId;
    private static String loggedInName;
    private static String loggedInEmail;
    private static String loggedInRole;

    /*
     * Prevents two scene changes from running at the same time.
     */
    private boolean sceneChanging;

    @FXML
    private void initialize() {
        messageLabel.setText("");
    }

    @FXML
    private void handleLogin() {
        if (sceneChanging) {
            return;
        }

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        showError("");

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter your email and password.");
            return;
        }

        String sql = """
                SELECT id, name, email, phone, password, address, role
                FROM users
                WHERE email = ? AND password = ?
                LIMIT 1
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    showError("Invalid email or password.");
                    passwordField.clear();
                    passwordField.requestFocus();
                    return;
                }

                loggedInUserId = resultSet.getInt("id");
                loggedInName = resultSet.getString("name");
                loggedInEmail = resultSet.getString("email");
                loggedInRole = resultSet.getString("role");

                String phone = resultSet.getString("phone");
                String savedPassword = resultSet.getString("password");
                String address = resultSet.getString("address");

                if (loggedInRole == null || loggedInRole.isBlank()) {
                    showError("This account has no assigned role.");
                    return;
                }

                /*
                 * Clear data left by an earlier login.
                 */
                Session.currentCustomer = null;
                Session.currentRestaurant = null;
                Session.currentOrder = null;

                if (loggedInRole.equalsIgnoreCase("CUSTOMER")) {

                    Session.currentCustomer = new Customer(
                            loggedInUserId,
                            loggedInName,
                            loggedInEmail,
                            phone,
                            savedPassword,
                            address
                    );

                    sceneChanging = true;
                    changeScene(CUSTOMER_PAGE);

                } else if (loggedInRole.equalsIgnoreCase("RIDER")) {

                    sceneChanging = true;
                    changeScene(RIDER_PAGE);

                } else {
                    showError("Unknown account role: " + loggedInRole);
                }
            }

        } catch (SQLException e) {
            sceneChanging = false;
            showError("Database error: " + e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            sceneChanging = false;
            showError("Unable to open the next page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToRegister() {
        if (sceneChanging) {
            return;
        }

        try {
            sceneChanging = true;
            changeScene(REGISTER_PAGE);

        } catch (IOException e) {
            sceneChanging = false;
            showError("Unable to open the registration page.");
            e.printStackTrace();
        }
    }

    private void changeScene(String fxmlFile) throws IOException {
        /*
         * Obtain the existing window before loading the next FXML.
         */
        Scene currentScene = emailField.getScene();

        if (currentScene == null || currentScene.getWindow() == null) {
            throw new IOException("The current application window is unavailable.");
        }

        Stage stage = (Stage) currentScene.getWindow();

        String resourcePath = "/com/projectreyna/" + fxmlFile;
        URL resource = getClass().getResource(resourcePath);

        if (resource == null) {
            throw new IOException("FXML file not found: " + resourcePath);
        }

        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    public static void clearLoginSession() {
        loggedInUserId = 0;
        loggedInName = null;
        loggedInEmail = null;
        loggedInRole = null;

        Session.currentCustomer = null;
        Session.currentRestaurant = null;
        Session.currentOrder = null;
    }

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static String getLoggedInName() {
        return loggedInName;
    }

    public static String getLoggedInEmail() {
        return loggedInEmail;
    }

    public static String getLoggedInRole() {
        return loggedInRole;
    }
}