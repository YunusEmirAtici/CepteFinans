package com.finanscepte.desktop.controller;

import com.finanscepte.desktop.api.ApiClient;
import com.finanscepte.desktop.auth.AuthManager;
import com.finanscepte.desktop.model.Budget;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BudgetsController {

    @FXML
    private TableView<Budget> budgetsTable;
    @FXML
    private TableColumn<Budget, String> categoryColumn;
    @FXML
    private TableColumn<Budget, BigDecimal> limitColumn;
    @FXML
    private TableColumn<Budget, BigDecimal> spentColumn;
    @FXML
    private TableColumn<Budget, Integer> monthColumn;
    @FXML
    private TableColumn<Budget, Integer> yearColumn;
    @FXML
    private TableColumn<Budget, Double> progressColumn;
    @FXML
    private TableColumn<Budget, String> statusColumn;
    @FXML
    private TableColumn<Budget, Void> actionColumn;

    @FXML
    private TextField categoryField;
    @FXML
    private TextField limitAmountField;
    @FXML
    private TextField spentAmountField;
    @FXML
    private TextField monthField;
    @FXML
    private TextField yearField;
    @FXML
    private Label budgetStatusLabel;
    @FXML
    private Label monthlySummaryLabel;

    private final ApiClient apiClient = new ApiClient();
    private final ObservableList<Budget> budgets = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureTable();
        yearField.setText(String.valueOf(LocalDate.now().getYear()));
        monthField.setText(String.valueOf(LocalDate.now().getMonthValue()));
        loadBudgets();
    }

    @FXML
    public void handleRefresh() {
        loadBudgets();
    }

    @FXML
    public void handleCreateBudget() {
        try {
            String category = categoryField.getText();
            BigDecimal limitAmount = new BigDecimal(limitAmountField.getText());
            BigDecimal spentAmount = spentAmountField.getText().isBlank() ? BigDecimal.ZERO : new BigDecimal(spentAmountField.getText());
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());

            if (category == null || category.isBlank()) {
                budgetStatusLabel.setText("Kategori zorunludur.");
                return;
            }

            String userId = AuthManager.getInstance().getUsername();
            apiClient.createBudget(userId, category, limitAmount, spentAmount, month, year);
            clearForm();
            loadBudgets();
            budgetStatusLabel.setText("Butce eklendi.");
        } catch (Exception e) {
            budgetStatusLabel.setText("Butce eklenemedi: " + e.getMessage());
        }
    }

    private void loadBudgets() {
        budgetStatusLabel.setText("Yukleniyor...");
        new Thread(() -> {
            try {
                String userId = AuthManager.getInstance().getUsername();
                List<Budget> response = apiClient.getBudgetsByUser(userId);
                Platform.runLater(() -> {
                    budgets.setAll(response);
                    budgetStatusLabel.setText("Toplam butce: " + response.size());
                    loadStatusSummary();
                });
            } catch (Exception e) {
                Platform.runLater(() -> budgetStatusLabel.setText("Butceler yuklenemedi: " + e.getMessage()));
            }
        }).start();
    }

    private void loadStatusSummary() {
        try {
            String userId = AuthManager.getInstance().getUsername();
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());
            List<Budget> monthBudgets = apiClient.getBudgetStatus(userId, month, year);
            long exceededCount = monthBudgets.stream().filter(Budget::exceeded).count();
            monthlySummaryLabel.setText("Aylik durum: " + monthBudgets.size() + " butce, " + exceededCount + " limit asimi");
        } catch (Exception e) {
            monthlySummaryLabel.setText("Aylik durum alinamadi: " + e.getMessage());
        }
    }

    private void configureTable() {
        categoryColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().category()));
        limitColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().limitAmount()));
        spentColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().spentAmount()));
        monthColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().month()));
        yearColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().year()));
        progressColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(calculateProgress(cell.getValue())));
        progressColumn.setCellFactory(col -> new TableCell<>() {
            private final ProgressBar progressBar = new ProgressBar(0);

            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                    return;
                }
                progressBar.setProgress(Math.min(value, 1.0));
                setGraphic(progressBar);
            }
        });
        statusColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().exceeded() ? "Limit asildi" : "Normal"));
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Sil");

            {
                deleteButton.getStyleClass().add("danger-button");
                deleteButton.setOnAction(event -> {
                    Budget budget = getTableView().getItems().get(getIndex());
                    handleDeleteBudget(budget);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        budgetsTable.setItems(budgets);
    }

    private double calculateProgress(Budget budget) {
        if (budget.limitAmount() == null || budget.limitAmount().compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        return budget.spentAmount().divide(budget.limitAmount(), 4, java.math.RoundingMode.HALF_UP).doubleValue();
    }

    private void handleDeleteBudget(Budget budget) {
        try {
            apiClient.deleteBudget(budget.id());
            budgets.remove(budget);
            budgetStatusLabel.setText("Butce silindi.");
            loadStatusSummary();
        } catch (Exception e) {
            budgetStatusLabel.setText("Butce silinemedi: " + e.getMessage());
        }
    }

    private void clearForm() {
        categoryField.clear();
        limitAmountField.clear();
        spentAmountField.clear();
    }
}
