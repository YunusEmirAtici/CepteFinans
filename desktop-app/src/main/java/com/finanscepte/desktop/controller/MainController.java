package com.finanscepte.desktop.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class MainController {

    @FXML
    private Canvas customCanvas;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        GraphicsContext gc = customCanvas.getGraphicsContext2D();
        gc.setFill(Color.web("#1e1e2f"));
        gc.fillRoundRect(20, 20, 300, 120, 20, 20);
        gc.setStroke(Color.web("#50fa7b"));
        gc.strokeText("FinansCepte Grafik Panel", 40, 80);
    }

    @FXML
    public void checkApiHealth() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            statusLabel.setText("Gateway yanit kodu: " + response.statusCode());
        } catch (Exception ex) {
            statusLabel.setText("Baglanti hatasi: " + ex.getMessage());
        }
    }
}
