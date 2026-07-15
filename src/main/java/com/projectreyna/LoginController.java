package com.projectreyna;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    static String username_login;
    public TextField tfUsername;
    public PasswordField pfPassword;
    public Label lblError;
    static boolean isDark = false;

    private String[] usernames = {"admin", "user"};
    private String[] passwords = {"pass123", "hello1"};

    public void onDarkModeClicked() {
        Scene scene = lblError.getScene();
        if (!isDark) {
            scene.getStylesheets().add(LoginController.class.getResource("dark.css").toExternalForm());
        } else {
            scene.getStylesheets().clear();
        }
        isDark = !isDark;
    }

    public void onSignInClicked() throws IOException {
        boolean success = false;
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        for (int i = 0; i < usernames.length; i++) {
            if (username.equals(usernames[i]) && password.equals(passwords[i])) {
                lblError.setText("Successfully logged in");
                username_login = usernames[i];
                success = true;
                Stage stage = (Stage) lblError.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("landing-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                break;
            }
        }
        if (!success) {
            lblError.setText("Invalid username or password");
        }
    }

    public void onPasswordKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onSignInClicked();
        }
    }
}
