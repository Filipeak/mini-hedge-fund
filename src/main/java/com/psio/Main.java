package com.psio;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.JSONMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import com.psio.market.MarketDataProvider;
import com.psio.trading.*;
import com.psio.ui.CryptoPortfolioApp;
import com.psio.ui.PortfolioChart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Notifier
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        // Agents
        TradingAgent tradingAgent1 = new ConservativeTradingAgent(new Wallet(10000.0f, 0.0f));
        marketDataNotifier.addObserver(tradingAgent1);

        TradingAgent tradingAgent2 = new SmartTradingAgent(new Wallet(10000.0f, 0.0f));
        marketDataNotifier.addObserver(tradingAgent2);

        List<TradingAgent> agents = new ArrayList<>();
        agents.add(tradingAgent1);
        agents.add(tradingAgent2);

        // Portfolio Chart
        PortfolioChart portfolioChart = new PortfolioChart(agents);
        marketDataNotifier.addPortfolioObserver(portfolioChart); // TODO change to use PortfolioManager

        // Report Creator
        // TODO ...

        // Application start
        CryptoPortfolioApp.start(args, portfolioChart, (file) -> loadAndProcessMarketData(file, portfolioChart, marketDataNotifier));

        System.out.println();
        tradingAgent1.getWalletInfo();
        System.out.println();
        tradingAgent2.getWalletInfo();
    }

    private static void loadAndProcessMarketData(File file, PortfolioChart portfolioChart, MarketDataNotifier marketDataNotifier) {
        Thread simulationThread = new Thread(() -> {
            MarketDataProvider provider;
            String path = file.getAbsolutePath();

            if (file.getName().toLowerCase().endsWith(".json")) {
                provider = new JSONMarketDataProvider(path);
            } else {
                provider = new CSVMarketDataProvider(path);
            }

            portfolioChart.onBegin();
            provider.getData(marketDataNotifier);
        });

        simulationThread.setDaemon(true);
        simulationThread.start();
    }
}