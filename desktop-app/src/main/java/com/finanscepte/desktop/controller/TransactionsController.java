package com.finanscepte.desktop.controller;

import com.finanscepte.desktop.api.ApiClient;
import com.finanscepte.desktop.auth.AuthManager;
import com.finanscepte.desktop.model.Transaction;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, String> categoryColumn;
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;
    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    @FXML
    private TableColumn<Transaction, Void> actionColumn;

    @FXML
    private TextField categoryField;
    @FXML
    private ComboBox<String> typeCombo;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker transactionDatePicker;
    @FXML
    private TextField descriptionField;
    @FXML
    private CheckBox recurringCheck;
    @FXML
    private Label transactionStatusLabel;

    private final ApiClient apiClient = new ApiClient();
    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        typeCombo.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE"));
        configureTable();
        loadTransactions();
    }

    @FXML
    public void handleRefresh() {
        loadTransactions();
    }

    @FXML
    public void handleCreateTransaction() {
        try {
            String category = categoryField.getText();
            String type = typeCombo.getValue();
            LocalDate transactionDate = transactionDatePicker.getValue();
            BigDecimal amount = new BigDecimal(amountField.getText());
            String description = descriptionField.getText();

            if (category == null || category.isBlank() || type == null || transactionDate == null) {
                transactionStatusLabel.setText("Kategori, tip ve tarih alanlari zorunludur.");
                return;
            }

            String userId = AuthManager.getInstance().getUsername();
            apiClient.createTransaction(userId, type, amount, category, transactionDate, description, recurringCheck.isSelected());
            clearForm();
            loadTransactions();
            transactionStatusLabel.setText("Islem eklendi.");
        } catch (Exception e) {
            transactionStatusLabel.setText("Islem eklenemedi: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        transactionStatusLabel.setText("Yukleniyor...");
        new Thread(() -> {
            try {
                String userId = AuthManager.getInstance().getUsername();
                List<Transaction> response = apiClient.getTransactions(userId);
                Platform.runLater(() -> {
                    transactions.setAll(response);
                    transactionStatusLabel.setText("Toplam islem: " + response.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> transactionStatusLabel.setText("Islemler yuklenemedi: " + e.getMessage()));
            }
        }).start();
    }

    private void configureTable() {
        categoryColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().category()));
        typeColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().type()));
        amountColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().amount()));
        dateColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().transactionDate()));
        descriptionColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().description()));
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Sil");

            {
                deleteButton.getStyleClass().add("danger-button");
                deleteButton.setOnAction(event -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    handleDeleteTransaction(transaction);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        transactionsTable.setItems(transactions);
    }

    private void handleDeleteTransaction(Transaction transaction) {
        try {
            apiClient.deleteTransaction(transaction.id());
            transactions.remove(transaction);
            transactionStatusLabel.setText("Islem silindi.");
        } catch (Exception e) {
            transactionStatusLabel.setText("Islem silinemedi: " + e.getMessage());
        }
    }

    private void clearForm() {
        categoryField.clear();
        typeCombo.getSelectionModel().clearSelection();
        amountField.clear();
        transactionDatePicker.setValue(null);
        descriptionField.clear();
        recurringCheck.setSelected(false);
    }
}
