package com.psio;

import com.psio.market.MarketDataNotifier;
import com.psio.trading.*;
import com.psio.ui.CryptoPortfolioApp;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        TradingAgent tradingAgent1 = new ConservativeTradingAgent(new Wallet(10000.0f, 0.0f));
        marketDataNotifier.addObserver(tradingAgent1);

        TradingAgent tradingAgent2 = new SmartTradingAgent(new Wallet(10000.0f, 0.0f));
        marketDataNotifier.addObserver(tradingAgent2);

        List<TradingAgent> agents = new ArrayList<>();
        agents.add(tradingAgent1);
        agents.add(tradingAgent2);

        CryptoPortfolioApp.main(args, agents, marketDataNotifier);

        System.out.println();
        tradingAgent1.getWalletInfo();
        System.out.println();
        tradingAgent2.getWalletInfo();
    }
}
