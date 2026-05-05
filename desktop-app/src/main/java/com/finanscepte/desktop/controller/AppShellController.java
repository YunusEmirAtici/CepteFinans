package com.finanscepte.desktop.controller;

import com.finanscepte.desktop.DesktopApp;
import com.finanscepte.desktop.auth.AuthManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppShellController {

    @FXML
    private Label userLabel;

    @FXML
    private StackPane contentPane;

    @FXML
    private Button dashboardNavButton;

    @FXML
    private Button transactionsNavButton;

    @FXML
    private Button budgetsNavButton;

    @FXML
    public void initialize() {
        userLabel.setText(AuthManager.getInstance().getUsername());
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        setActiveButton(dashboardNavButton);
        loadContent("/dashboard-view.fxml");
    }

    @FXML
    public void showTransactions() {
        setActiveButton(transactionsNavButton);
        loadContent("/transactions-view.fxml");
    }

    @FXML
    public void showBudgets() {
        setActiveButton(budgetsNavButton);
        loadContent("/budgets-view.fxml");
    }

    @FXML
    public void handleLogout() {
        AuthManager.getInstance().clear();
        try {
            Stage stage = (Stage) contentPane.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(DesktopApp.class.getResource("/login-view.fxml")), 400, 500);
            scene.getStylesheets().add(DesktopApp.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("FinansCepte - Giris");
            stage.setResizable(false);
        } catch (Exception e) {
            userLabel.setText("Cikis hatasi: " + e.getMessage());
        }
    }

    private void loadContent(String resourcePath) {
        try {
            Node content = FXMLLoader.load(DesktopApp.class.getResource(resourcePath));
            contentPane.getChildren().setAll(content);
        } catch (Exception e) {
            Label fallback = new Label("Ekran yuklenemedi: " + e.getMessage());
            fallback.getStyleClass().add("error-label");
            contentPane.getChildren().setAll(fallback);
        }
    }

    private void setActiveButton(Button activeButton) {
        dashboardNavButton.getStyleClass().remove("sidebar-button-active");
        transactionsNavButton.getStyleClass().remove("sidebar-button-active");
        budgetsNavButton.getStyleClass().remove("sidebar-button-active");
        activeButton.getStyleClass().add("sidebar-button-active");
    }
}
