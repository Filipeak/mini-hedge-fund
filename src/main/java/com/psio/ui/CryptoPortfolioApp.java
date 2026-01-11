package com.psio.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CryptoPortfolioApp extends Application {

    private static final Logger logger = LogManager.getLogger(CryptoPortfolioApp.class);

    public static final String CSS_PATH = "/styles.css";
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private static Consumer<File> fileImportHandler;
    private static PortfolioChart portfolioChart;

    private VBox loadingOverlay;

    public void start(Stage primaryStage) {
        BorderPane mainContent = new BorderPane();

        // 1. Center: Chart
        mainContent.setCenter(portfolioChart.createChart());

        // 2. Top: Menu
        AppMenu appMenu = new AppMenu(primaryStage, this::onFileSelected);
        mainContent.setTop(appMenu.createMenu());

        // 3. Bottom: Controls Bar
        HBox bottomBar = createBottomBar();
        mainContent.setBottom(bottomBar);

        createLoadingOverlay();

        StackPane root = new StackPane(mainContent, loadingOverlay);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm());
        } catch (Exception e) {
            logger.error("No styles: {}", e.getMessage());
        }

        primaryStage.setTitle("FinTech Portfolio Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createLoadingOverlay() {
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.getStyleClass().add("progress-indicator");
        spinner.setMaxSize(60, 60);

        Label loadingLabel = new Label("Przetwarzanie danych...");
        loadingLabel.getStyleClass().add("loading-label");

        loadingOverlay = new VBox(20, spinner, loadingLabel);
        loadingOverlay.getStyleClass().add("loading-overlay");
        loadingOverlay.setAlignment(Pos.CENTER);
        loadingOverlay.setVisible(false);
    }

    private void executeAction(Runnable backgroundTask, Runnable uiUpdate) {
        loadingOverlay.setVisible(true);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(200);

                if (backgroundTask != null) {
                    backgroundTask.run();
                }
            } catch (Exception e) {
                logger.error("Błąd w tle: ", e);
            } finally {
                Platform.runLater(() -> {
                    if (uiUpdate != null) {
                        uiUpdate.run();
                    }
                    loadingOverlay.setVisible(false);
                });
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private HBox createBottomBar() {
        CheckBox toggleViewMode = new CheckBox("Pokaż procentowy zwrot");

        toggleViewMode.getStyleClass().add("view-toggle");

        toggleViewMode.setOnAction(e -> {
            if (portfolioChart != null) {
                executeAction(
                        () -> portfolioChart.toggleViewModeInternal(),
                        () -> portfolioChart.refreshSeries()
                );
            }
        });

        Label labelSource = new Label("Źródło danych:");
        labelSource.getStyleClass().add("chart-selector-label");

        ComboBox<String> sourceSelector = new ComboBox<>();
        sourceSelector.getStyleClass().add("chart-selector");

        sourceSelector.getItems().addAll(portfolioChart.getAvailableDataSources());
        sourceSelector.getSelectionModel().select(portfolioChart.getDefaultDataSourceName());

        sourceSelector.setMinWidth(200);

        sourceSelector.setOnAction(e -> {
            String selected = sourceSelector.getValue();
            if (selected != null && portfolioChart != null) {
                executeAction(
                        () -> portfolioChart.setDataSourceInternal(selected),
                        () -> portfolioChart.refreshSeries()
                );
            }
        });

        HBox bottomBox = new HBox(toggleViewMode, labelSource, sourceSelector);
        bottomBox.getStyleClass().add("bottom-status-bar");
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(5);

        return bottomBox;
    }

    private void onFileSelected(File file) {
        if (file == null || !file.exists()) {
            logger.info("File Not Found");
            return;
        }

        executeAction(
                () -> {
                    if (fileImportHandler != null) fileImportHandler.accept(file);
                },
                () -> {
                    if (portfolioChart != null) portfolioChart.refreshSeries();
                }
        );
    }

    public static void start(String[] args, PortfolioChart portfolioChart, Consumer<File> fileImportHandler) {
        CryptoPortfolioApp.portfolioChart = portfolioChart;
        CryptoPortfolioApp.fileImportHandler = fileImportHandler;

        launch(args);
    }
}