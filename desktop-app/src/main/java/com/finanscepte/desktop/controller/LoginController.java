package com.finanscepte.desktop.controller;

import com.finanscepte.desktop.DesktopApp;
import com.finanscepte.desktop.api.ApiClient;
import com.finanscepte.desktop.auth.AuthManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final ApiClient apiClient = new ApiClient();

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Email ve sifre bos birakilamaz!");
            return;
        }

        try {
            String token = apiClient.login(email, password);
            AuthManager.getInstance().setToken(token);
            AuthManager.getInstance().setUsername(email);

            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(DesktopApp.class.getResource("/app-shell.fxml")), 1280, 760);
            scene.getStylesheets().add(DesktopApp.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("FinansCepte - " + email);
            stage.setResizable(true);
        } catch (Exception e) {
            errorLabel.setText("Giris basarisiz: " + e.getMessage());
        }
    }
}
