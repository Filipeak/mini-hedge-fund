package com.psio;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import com.psio.trading.*;
import com.psio.ui.CryptoPortfolioApp;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CSVMarketDataProvider csvMarketDataProvider = new CSVMarketDataProvider("src/main/resources/data.csv");
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        TradingAgent tradingAgent1 = new ConservativeTradingAgent(new Wallet(10000.0f, 0.0f));
        marketDataNotifier.addObserver(tradingAgent1);

        TradingAgent tradingAgent2 = new SmartTradingAgent(new Wallet(10000.0f, 0.0f));
        marketDataNotifier.addObserver(tradingAgent2);

        List<TradingAgent> agentsList = new ArrayList<>();
        agentsList.add(tradingAgent1);
        agentsList.add(tradingAgent2);

        CryptoPortfolioApp.injectData(agentsList, marketDataNotifier);

        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            System.out.println("Start symulacji z Main...");
            csvMarketDataProvider.getData(marketDataNotifier);
        }).start();

        CryptoPortfolioApp.main(args);

        System.out.println("\n--- Symulacja Zakończona ---");
        tradingAgent1.getWalletInfo();
        tradingAgent2.getWalletInfo();
    }
}