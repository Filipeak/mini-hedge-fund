package com.psio.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

public class CryptoPortfolioApp extends Application {

    public static final String CSS_PATH = "/styles.css";
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private static Consumer<File> fileImportHandler;
    private static PortfolioChart portfolioChart;

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setCenter(portfolioChart.createChart());

        AppMenu appMenu = new AppMenu(primaryStage, this::onFileSelected);
        root.setTop(appMenu.createMenu());

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm());
        } catch (Exception e) {
            System.err.println("No styles: " + e.getMessage());
        }

        primaryStage.setTitle("FinTech Portfolio Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void onFileSelected(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        System.out.println("UI: Preparing chart for new simulation...");

        if (fileImportHandler != null) {
            fileImportHandler.accept(file);
        }
    }

    public static void start(String[] args, PortfolioChart portfolioChart, Consumer<File> fileImportHandler) {
        CryptoPortfolioApp.portfolioChart = portfolioChart;
        CryptoPortfolioApp.fileImportHandler = fileImportHandler;

        launch(args);
    }
}