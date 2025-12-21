package com.psio;

import com.psio.market.MarketDataNotifier;
import com.psio.portfolio.PortfolioManager;
import com.psio.trading.*;
import com.psio.ui.CryptoPortfolioApp;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TradingAgent tradingAgent1 = new ConservativeTradingAgent(new Wallet(10000.0f, 0.0f));
        TradingAgent tradingAgent2 = new SmartTradingAgent(new Wallet(10000.0f, 0.0f));

        TradingAgent[] tradingAgents = new TradingAgent[]{tradingAgent1, tradingAgent2};

        PortfolioManager portfolioManager = new PortfolioManager(tradingAgents);

        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        marketDataNotifier.addObserver(portfolioManager);


        List<TradingAgent> agents = new ArrayList<>();
        agents.add(tradingAgent1);
        agents.add(tradingAgent2);

        CryptoPortfolioApp.main(args, agents, marketDataNotifier);


        System.out.println();
        System.out.println("The value of all wallets: " + portfolioManager.getCurrentValue(90032.31f));
        //Magic number used for testing is the last open value of the data set
    }
}
