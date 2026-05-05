package com.finanscepte.desktop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finanscepte.desktop.auth.AuthManager;
import com.finanscepte.desktop.model.Budget;
import com.finanscepte.desktop.model.Transaction;
import com.finanscepte.desktop.model.User;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ApiClient() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    private HttpRequest.Builder requestBuilder(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json");
        String token = AuthManager.getInstance().getToken();
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        return builder;
    }

    public String login(String email, String password) throws Exception {
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        HttpRequest request = requestBuilder("/api/auth/login")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            var node = mapper.readTree(response.body());
            if (node.hasNonNull("accessToken")) {
                return node.get("accessToken").asText();
            }
            if (node.hasNonNull("token")) {
                return node.get("token").asText();
            }
            throw new RuntimeException("Token response is invalid");
        }
        throw new RuntimeException("Login failed: " + response.statusCode());
    }

    public List<User> getUsers() throws Exception {
        HttpRequest request = requestBuilder("/api/users").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        }
        return List.of();
    }

    public List<Transaction> getTransactions(String userId) throws Exception {
        String encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8);
        HttpRequest request = requestBuilder("/api/transactions?userId=" + encodedUserId).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, Transaction.class));
        }
        return List.of();
    }

    public Transaction createTransaction(String userId,
                                         String type,
                                         java.math.BigDecimal amount,
                                         String category,
                                         LocalDate transactionDate,
                                         String description,
                                         boolean recurring) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("type", type);
        payload.put("amount", amount);
        payload.put("category", category);
        payload.put("transactionDate", transactionDate);
        payload.put("description", description);
        payload.put("recurring", recurring);

        String json = mapper.writeValueAsString(payload);
        HttpRequest request = requestBuilder("/api/transactions")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            return mapper.readValue(response.body(), Transaction.class);
        }
        throw new RuntimeException("Transaction create failed: " + response.statusCode());
    }

    public void deleteTransaction(String transactionId) throws Exception {
        HttpRequest request = requestBuilder("/api/transactions/" + transactionId)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204) {
            throw new RuntimeException("Transaction delete failed: " + response.statusCode());
        }
    }

    public List<Budget> getBudgetsByUser(String userId) throws Exception {
        String encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8);
        HttpRequest request = requestBuilder("/api/budgets/user/" + encodedUserId).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, Budget.class));
        }
        return List.of();
    }

    public List<Budget> getBudgetStatus(String userId, int month, int year) throws Exception {
        String encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8);
        HttpRequest request = requestBuilder("/api/budgets/user/" + encodedUserId + "/status?month=" + month + "&year=" + year).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, Budget.class));
        }
        return List.of();
    }

    public Budget createBudget(String userId,
                               String category,
                               java.math.BigDecimal limitAmount,
                               java.math.BigDecimal spentAmount,
                               int month,
                               int year) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("category", category);
        payload.put("limitAmount", limitAmount);
        payload.put("spentAmount", spentAmount);
        payload.put("month", month);
        payload.put("year", year);

        String json = mapper.writeValueAsString(payload);
        HttpRequest request = requestBuilder("/api/budgets")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            return mapper.readValue(response.body(), Budget.class);
        }
        throw new RuntimeException("Budget create failed: " + response.statusCode());
    }

    public void deleteBudget(String budgetId) throws Exception {
        HttpRequest request = requestBuilder("/api/budgets/" + budgetId)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204) {
            throw new RuntimeException("Budget delete failed: " + response.statusCode());
        }
    }
}
