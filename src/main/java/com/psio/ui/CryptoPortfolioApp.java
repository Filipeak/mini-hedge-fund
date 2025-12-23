package com.psio.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

        // 1. Center: Chart
        root.setCenter(portfolioChart.createChart());

        // 2. Top: Menu
        AppMenu appMenu = new AppMenu(primaryStage, this::onFileSelected);
        root.setTop(appMenu.createMenu());

        // 3. Bottom: Toggle Bar
        HBox bottomBar = createBottomBar();
        root.setBottom(bottomBar);

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

    private HBox createBottomBar() {
        CheckBox toggleViewMode = new CheckBox("Pokaż procentowy zwrot");

        toggleViewMode.getStyleClass().add("view-toggle");

        toggleViewMode.setOnAction(e -> {
            if (portfolioChart != null) {
                portfolioChart.toggleViewMode();
            }
        });

        HBox bottomBox = new HBox(toggleViewMode);
        bottomBox.getStyleClass().add("bottom-status-bar");
        bottomBox.setAlignment(Pos.CENTER);

        return bottomBox;
    }

    private void onFileSelected(File file) {
        if (file == null || !file.exists()) {
            System.out.println("File Not Found");
            return;
        }
        if (fileImportHandler != null) fileImportHandler.accept(file);
    }

    public static void start(String[] args, PortfolioChart portfolioChart, Consumer<File> fileImportHandler) {
        CryptoPortfolioApp.portfolioChart = portfolioChart;
        CryptoPortfolioApp.fileImportHandler = fileImportHandler;

        launch(args);
    }
}