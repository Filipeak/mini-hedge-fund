package com.psio.ui;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.JSONMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import com.psio.market.MarketDataProvider;
import com.psio.trading.TradingAgent;
import com.psio.trading.Wallet;
import com.psio.trading.ConservativeTradingAgent;
import com.psio.trading.SmartTradingAgent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CryptoPortfolioApp extends Application {

    public static final String CSS_PATH = "/styles.css";
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private static List<TradingAgent> injectedAgents;
    private static MarketDataNotifier injectedNotifier;

    private PortfolioChart portfolioChart;
    private MarketDataNotifier marketDataNotifier;

    public static void injectData(List<TradingAgent> agents, MarketDataNotifier notifier) {
        injectedAgents = agents;
        injectedNotifier = notifier;
    }

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        List<TradingAgent> agents;

        if (injectedAgents != null && injectedNotifier != null) {
            System.out.println("UI: Używam danych dostarczonych z Maina.");
            agents = injectedAgents;
            this.marketDataNotifier = injectedNotifier;
        } else {
            System.out.println("UI: Brak danych z Maina - tryb samodzielny (testowy).");
            TradingAgent agent1 = new ConservativeTradingAgent(new Wallet(10000.0f, 0.0f));
            TradingAgent agent2 = new SmartTradingAgent(new Wallet(10000.0f, 0.0f));
            agents = new ArrayList<>();
            agents.add(agent1);
            agents.add(agent2);
            this.marketDataNotifier = new MarketDataNotifier();
            this.marketDataNotifier.addObserver(agent1);
            this.marketDataNotifier.addObserver(agent2);
        }

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