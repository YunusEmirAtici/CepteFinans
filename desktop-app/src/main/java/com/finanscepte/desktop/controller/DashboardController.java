package com.finanscepte.desktop.controller;

import com.finanscepte.desktop.DesktopApp;
import com.finanscepte.desktop.api.ApiClient;
import com.finanscepte.desktop.auth.AuthManager;
import com.finanscepte.desktop.model.Budget;
import com.finanscepte.desktop.model.Transaction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Label totalExpenseLabel;

    @FXML
    private Label netBalanceLabel;

    @FXML
    private Canvas pieChartCanvas;

    @FXML
    private Canvas barChartCanvas;

    @FXML
    private VBox budgetAlertsBox;

    private final ApiClient apiClient = new ApiClient();

    @FXML
    public void initialize() {
        welcomeLabel.setText("Hosgeldin, " + AuthManager.getInstance().getUsername());
        loadDashboardData();
    }

    private void loadDashboardData() {
        new Thread(() -> {
            try {
                String userId = AuthManager.getInstance().getUsername();
                List<Transaction> transactions = apiClient.getTransactions(userId);
                List<Budget> budgets = apiClient.getBudgetsByUser(userId);

                Platform.runLater(() -> {
                    updateSummaryCards(transactions);
                    drawPieChart(transactions);
                    drawBarChart(transactions);
                    updateBudgetAlerts(budgets);
                });
            } catch (Exception e) {
                Platform.runLater(() -> welcomeLabel.setText("Veri yuklenemedi: " + e.getMessage()));
            }
        }).start();
    }

    private void updateSummaryCards(List<Transaction> transactions) {
        BigDecimal income = transactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.type()))
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.type()))
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal net = income.subtract(expense);

        totalIncomeLabel.setText(String.format("%,.2f TL", income));
        totalExpenseLabel.setText(String.format("%,.2f TL", expense));
        netBalanceLabel.setText(String.format("%,.2f TL", net));
        netBalanceLabel.setStyle(net.compareTo(BigDecimal.ZERO) >= 0 ? "-fx-text-fill: #50fa7b;" : "-fx-text-fill: #ff5555;");
    }

    private void drawPieChart(List<Transaction> transactions) {
        GraphicsContext gc = pieChartCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, pieChartCanvas.getWidth(), pieChartCanvas.getHeight());

        Map<String, BigDecimal> categoryTotals = transactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.type()))
                .collect(Collectors.groupingBy(Transaction::category,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::amount, BigDecimal::add)));

        if (categoryTotals.isEmpty()) {
            gc.setFill(Color.web("#bbbbbb"));
            gc.setFont(Font.font(14));
            gc.fillText("Harcama verisi bulunamadi", 20, 30);
            return;
        }

        BigDecimal total = categoryTotals.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(BigDecimal.ZERO) == 0) return;

        double centerX = 150;
        double centerY = 150;
        double radius = 120;
        double startAngle = 0;

        String[] colors = {"#ff5555", "#50fa7b", "#8be9fd", "#f1fa8c", "#bd93f9", "#ffb86c"};
        int colorIndex = 0;

        double legendX = 320;
        double legendY = 40;

        for (Map.Entry<String, BigDecimal> entry : categoryTotals.entrySet()) {
            double ratio = entry.getValue().doubleValue() / total.doubleValue();
            double angle = ratio * 360;

            gc.setFill(Color.web(colors[colorIndex % colors.length]));
            gc.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, angle, javafx.scene.shape.ArcType.ROUND);

            gc.setFill(Color.web(colors[colorIndex % colors.length]));
            gc.fillRect(legendX, legendY + colorIndex * 25, 15, 15);
            gc.setFill(Color.web("#f8f8f2"));
            gc.setFont(Font.font(12));
            gc.fillText(entry.getKey() + " (" + String.format("%.1f", ratio * 100) + "%)", legendX + 22, legendY + colorIndex * 25 + 12);

            startAngle += angle;
            colorIndex++;
        }

        gc.setFill(Color.web("#f8f8f2"));
        gc.setFont(Font.font(16));
        gc.fillText("Kategori Bazli Harcamalar", 10, 20);
    }

    private void drawBarChart(List<Transaction> transactions) {
        GraphicsContext gc = barChartCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, barChartCanvas.getWidth(), barChartCanvas.getHeight());

        Map<String, BigDecimal> incomeByMonth = transactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.type()))
                .collect(Collectors.groupingBy(
                        t -> t.transactionDate() != null ? t.transactionDate().getMonth().toString().substring(0, 3) : "N/A",
                        Collectors.reducing(BigDecimal.ZERO, Transaction::amount, BigDecimal::add)));

        Map<String, BigDecimal> expenseByMonth = transactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.type()))
                .collect(Collectors.groupingBy(
                        t -> t.transactionDate() != null ? t.transactionDate().getMonth().toString().substring(0, 3) : "N/A",
                        Collectors.reducing(BigDecimal.ZERO, Transaction::amount, BigDecimal::add)));

        if (incomeByMonth.isEmpty() && expenseByMonth.isEmpty()) {
            gc.setFill(Color.web("#bbbbbb"));
            gc.setFont(Font.font(14));
            gc.fillText("Veri bulunamadi", 20, 30);
            return;
        }

        double maxVal = Math.max(
                incomeByMonth.values().stream().mapToDouble(BigDecimal::doubleValue).max().orElse(0),
                expenseByMonth.values().stream().mapToDouble(BigDecimal::doubleValue).max().orElse(0)
        );

        if (maxVal == 0) maxVal = 1;

        double chartX = 60;
        double chartY = 40;
        double chartW = 500;
        double chartH = 220;

        gc.setStroke(Color.web("#6272a4"));
        gc.strokeRect(chartX, chartY, chartW, chartH);

        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN"};
        double barWidth = 30;
        double spacing = 70;

        for (int i = 0; i < months.length; i++) {
            double x = chartX + 20 + i * spacing;
            double incomeH = (incomeByMonth.getOrDefault(months[i], BigDecimal.ZERO).doubleValue() / maxVal) * (chartH - 20);
            double expenseH = (expenseByMonth.getOrDefault(months[i], BigDecimal.ZERO).doubleValue() / maxVal) * (chartH - 20);

            gc.setFill(Color.web("#50fa7b"));
            gc.fillRect(x, chartY + chartH - incomeH - 1, barWidth / 2, incomeH);

            gc.setFill(Color.web("#ff5555"));
            gc.fillRect(x + barWidth / 2 + 2, chartY + chartH - expenseH - 1, barWidth / 2, expenseH);

            gc.setFill(Color.web("#f8f8f2"));
            gc.setFont(Font.font(10));
            gc.fillText(months[i], x + 5, chartY + chartH + 15);
        }

        gc.setFill(Color.web("#50fa7b"));
        gc.fillRect(chartX + chartW - 100, chartY + 5, 12, 12);
        gc.setFill(Color.web("#f8f8f2"));
        gc.fillText("Gelir", chartX + chartW - 85, chartY + 15);

        gc.setFill(Color.web("#ff5555"));
        gc.fillRect(chartX + chartW - 100, chartY + 22, 12, 12);
        gc.setFill(Color.web("#f8f8f2"));
        gc.fillText("Gider", chartX + chartW - 85, chartY + 32);

        gc.setFill(Color.web("#f8f8f2"));
        gc.setFont(Font.font(16));
        gc.fillText("Aylik Gelir - Gider Karsilastirmasi", 10, 20);
    }

    private void updateBudgetAlerts(List<Budget> budgets) {
        budgetAlertsBox.getChildren().clear();
        List<Budget> exceeded = budgets.stream().filter(Budget::exceeded).toList();

        if (exceeded.isEmpty()) {
            Label okLabel = new Label("Tum butceler limit icinde!");
            okLabel.setStyle("-fx-text-fill: #50fa7b; -fx-font-size: 14px;");
            budgetAlertsBox.getChildren().add(okLabel);
        } else {
            for (Budget b : exceeded) {
                Label alertLabel = new Label("ALERT: " + b.category() + " butcesi asildi! (Limit: " + b.limitAmount() + ", Harcanan: " + b.spentAmount() + ")");
                alertLabel.setStyle("-fx-text-fill: #ff5555; -fx-font-size: 13px; -fx-padding: 4px;");
                budgetAlertsBox.getChildren().add(alertLabel);
            }
        }
    }

    @FXML
    public void handleLogout() {
        AuthManager.getInstance().clear();
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(DesktopApp.class.getResource("/login-view.fxml")), 400, 500);
            scene.getStylesheets().add(DesktopApp.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("FinansCepte - Giris");
        } catch (Exception e) {
            welcomeLabel.setText("Cikis hatasi: " + e.getMessage());
        }
    }
}
