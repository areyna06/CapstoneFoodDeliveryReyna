package com.projectreyna;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        // fill the account-type dropdown (this is why it was empty)
        roleComboBox.getItems().addAll("CUSTOMER", "RIDER");
        roleComboBox.setValue("CUSTOMER");
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        // validation
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            messageLabel.setText("Name, email and password are required.");
            return;
        }
        if (!password.equals(confirm)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }
        if (role == null) {
            messageLabel.setText("Please select an account type.");
            return;
        }

        String sql = "INSERT INTO users (name, email, phone, password, address, role) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.setString(5, address);
            ps.setString(6, role);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            int newId = keys.next() ? keys.getInt(1) : -1;

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Account created! You can now log in.");

            // send them back to login to sign in with the new account
            SceneSwitcher.switchTo("login-view.fxml", nameField);

        } catch (SQLIntegrityConstraintViolationException dup) {
            messageLabel.setText("That email is already registered.");
        } catch (SQLException e) {
            messageLabel.setText("Database error — is XAMPP MySQL running?");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin() {
        SceneSwitcher.switchTo("login-view.fxml", nameField);
    }
}