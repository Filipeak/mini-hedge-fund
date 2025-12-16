package com.psio.ui;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.JSONMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import com.psio.market.MarketDataProvider;
import com.psio.trading.TradingAgent;
import javafx.application.Application;
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

    private PortfolioChart portfolioChart;
    private MarketDataNotifier marketDataNotifier;

    public void start(Stage primaryStage) {
        List<TradingAgent> agents = SimulationContext.getAgents();
        this.marketDataNotifier = SimulationContext.getNotifier();

        if (agents == null || marketDataNotifier == null) {
            throw new RuntimeException("BŁĄD: Uruchom aplikację przez Main.java! Brak danych symulacji.");
        }

        BorderPane root = new BorderPane();

        portfolioChart = new PortfolioChart(agents);
        this.marketDataNotifier.addObserver(portfolioChart);
        root.setCenter(portfolioChart.createChart());

        AppMenu appMenu = new AppMenu(primaryStage, this::runSimulation);
        root.setTop(appMenu.createMenu());

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm());
        } catch (Exception e) { System.err.println("Brak stylów: " + e.getMessage()); }

        primaryStage.setTitle("FinTech Portfolio Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void runSimulation(File file) {
        if (file == null || !file.exists()) return;
        System.out.println("UI: Uruchamianie nowej symulacji dla pliku: " + file.getName());

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
            provider.getData(marketDataNotifier);
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}