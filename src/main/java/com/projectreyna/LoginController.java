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

    /*
     * Dependency Inversion Principle (DIP):
     * The controller depends on the UserRepository abstraction,
     * not on the concrete database class. All SQL code was moved
     * out of this controller into MySqlUserRepository (SRP).
     */
    private final UserRepository userRepository = new MySqlUserRepository();

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

        SessionData user = userRepository.findByCredentials(email, password);

        if (user == null) {
            showError("Invalid email or password.");
            passwordField.clear();
            passwordField.requestFocus();
            return;
        }

        String role = user.getRole();

        if (role == null || role.isBlank()) {
            showError("This account has no assigned role.");
            return;
        }

        /*
         * Clear data left by an earlier login.
         */
        Session.currentCustomer = null;
        Session.currentRestaurant = null;
        Session.currentOrder = null;

        /*
         * Serialization: write the logged-in user's information
         * into session.dat. The rest of the application reads this
         * file to validate and maintain the session.
         */
        SessionManager.createSession(user);

        try {
            if (role.equalsIgnoreCase("CUSTOMER")) {

                Session.currentCustomer = new Customer(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        password,
                        user.getAddress()
                );

                sceneChanging = true;
                changeScene(CUSTOMER_PAGE);

            } else if (role.equalsIgnoreCase("RIDER")) {

                sceneChanging = true;
                changeScene(RIDER_PAGE);

            } else {
                SessionManager.destroySession();
                showError("Unknown account role: " + role);
            }

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

    /**
     * Logout: deletes session.dat and clears in-memory session data.
     */
    public static void clearLoginSession() {
        SessionManager.destroySession();

        Session.currentCustomer = null;
        Session.currentRestaurant = null;
        Session.currentOrder = null;
    }

    /*
     * The getters below read the serialized session.dat file through
     * SessionManager, so the application literally uses the serialized
     * file to maintain the user's session while navigating.
     */
    public static int getLoggedInUserId() {
        SessionData session = SessionManager.getSession();
        return session == null ? 0 : session.getId();
    }

    public static String getLoggedInName() {
        SessionData session = SessionManager.getSession();
        return session == null ? null : session.getName();
    }

    public static String getLoggedInEmail() {
        SessionData session = SessionManager.getSession();
        return session == null ? null : session.getEmail();
    }

    public static String getLoggedInRole() {
        SessionData session = SessionManager.getSession();
        return session == null ? null : session.getRole();
    }
}
