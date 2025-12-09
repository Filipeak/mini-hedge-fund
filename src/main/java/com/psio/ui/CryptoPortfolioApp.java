package com.psio.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class CryptoPortfolioApp extends Application {
    public static final String CSS_PATH = "/styles.css";
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    public void start(Stage primaryStage) {
        DataLoader dataLoader = new DataLoader();
        PortfolioChart portfolioChart = new PortfolioChart();

        BorderPane root = new BorderPane();

        AppMenu appMenu = new AppMenu(primaryStage, dataLoader, portfolioChart);
        root.setTop(appMenu.createMenu());

        root.setCenter(portfolioChart.createChart());

        List<PortfolioData> initialData = dataLoader.loadDataFromResource("/data.csv");
        portfolioChart.updateData(initialData);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm());

        primaryStage.setTitle("FinTech Portfolio Tracker - Simple & Clean");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}