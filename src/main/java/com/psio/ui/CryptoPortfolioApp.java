package com.psio.ui;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import com.psio.trading.*;
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

    private PortfolioChart portfolioChart;
    private MarketDataNotifier marketDataNotifier;

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        TradingAgent agent1 = new ConservativeTradingAgent(new Wallet(10000.0f, 0.0f));
        TradingAgent agent2 = new SmartTradingAgent(new Wallet(10000.0f, 0.0f));

        List<TradingAgent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        marketDataNotifier = new MarketDataNotifier();
        marketDataNotifier.addObserver(agent1);
        marketDataNotifier.addObserver(agent2);

        portfolioChart = new PortfolioChart(agents);
        marketDataNotifier.addObserver(portfolioChart);
        root.setCenter(portfolioChart.createChart());

        AppMenu appMenu = new AppMenu(primaryStage, this::runSimulation);
        root.setTop(appMenu.createMenu());

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm()
        );

        primaryStage.setTitle("FinTech Portfolio Tracker - Maciek Integration");
        primaryStage.setScene(scene);
        primaryStage.show();

        runSimulation(new File("src/main/resources/data.csv"));
    }

    private void runSimulation(File file) {
        if (file == null || !file.exists()) return;

        System.out.println("Uruchamianie symulacji dla pliku: " + file.getAbsolutePath());

        portfolioChart.clear();

        Thread simulationThread = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            CSVMarketDataProvider provider = new CSVMarketDataProvider(file.getAbsolutePath());

            provider.getData(marketDataNotifier);
        });

        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}