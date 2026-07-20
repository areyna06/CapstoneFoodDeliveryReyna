package com.projectreyna;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class RiderController {

    private static final String LOGIN_PAGE = "login-view.fxml";
    private static final String TRACKING_PAGE = "tracking-view.fxml";

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        /*
         * Session validation: this screen reads the serialized
         * session.dat file. If it does not exist, the user is
         * sent back to the login screen.
         */
        SessionData session = SessionManager.getSession();

        if (session == null) {
            Platform.runLater(this::redirectToLogin);
            return;
        }

        welcomeLabel.setText("Welcome, " + session.getEmail() + "!");
        messageLabel.setText("");
    }

    @FXML
    private void handleOpenTracking() {
        if (Session.currentOrder == null) {
            messageLabel.setText(
                    "You currently have no assigned delivery."
            );
            return;
        }

        try {
            changeScene(TRACKING_PAGE);
        } catch (IOException e) {
            messageLabel.setText(
                    "Unable to open order tracking."
            );
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        /*
         * Logout: session.dat is deleted automatically and the
         * user is redirected to the login screen.
         */
        LoginController.clearLoginSession();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            changeScene(LOGIN_PAGE);
        } catch (IOException e) {
            messageLabel.setText("Unable to return to login.");
            e.printStackTrace();
        }
    }

    private void changeScene(String fxmlFile) throws IOException {
        String path = "/com/projectreyna/" + fxmlFile;
        URL resource = getClass().getResource(path);

        if (resource == null) {
            throw new IOException("FXML file not found: " + path);
        }

        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();

        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }
}
