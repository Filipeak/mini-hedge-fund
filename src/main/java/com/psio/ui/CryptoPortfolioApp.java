package com.psio.ui;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.JSONMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import com.psio.market.MarketDataProvider;
import com.psio.trading.TradingAgent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class CryptoPortfolioApp extends Application {

    public static final String CSS_PATH = "/styles.css";
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private static List<TradingAgent> injectedAgents;
    private static MarketDataNotifier injectedNotifier;

    private PortfolioChart portfolioChart;

    public void start(Stage primaryStage) {
        if (injectedAgents == null || injectedNotifier == null) {
            System.err.println("ERROR: Run application via Main.java. No simulation data.");
            Platform.exit();
            return;
        }

        BorderPane root = new BorderPane();

        portfolioChart = new PortfolioChart(injectedAgents);
        injectedNotifier.addObserver(portfolioChart);
        root.setCenter(portfolioChart.createChart());

        AppMenu appMenu = new AppMenu(primaryStage, this::runSimulation);
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

    private void runSimulation(File file) {
        if (file == null || !file.exists()) return;

        System.out.println("UI: Starting a new simulation for a file: " + file.getName());
        portfolioChart.clear();

        Thread simulationThread = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            MarketDataProvider provider;
            String path = file.getAbsolutePath();

            if (file.getName().toLowerCase().endsWith(".json")) {
                provider = new JSONMarketDataProvider(path);
            } else {
                provider = new CSVMarketDataProvider(path);
            }
            provider.getData(injectedNotifier);
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    public static void main(String[] args, List<TradingAgent> agents, MarketDataNotifier notifier) {
        injectedAgents = agents;
        injectedNotifier = notifier;

        launch(args);
    }
}