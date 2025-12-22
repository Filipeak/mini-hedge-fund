package com.psio;

import com.psio.market.MarketDataNotifier;
import com.psio.portfolio.PortfolioManager;
import com.psio.trading.*;
import com.psio.trading.agents.ConservativeTradingAgent;
import com.psio.trading.agents.SmartTradingAgent;
import com.psio.trading.agents.TradingAgent;
import com.psio.ui.CryptoPortfolioApp;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final float defaultBalance = 10000.0f;
        final float defaultAssetAmount = 0.0f;

        TradingAgent tradingAgent1 = new ConservativeTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Conservative wallet"));
        TradingAgent tradingAgent2 = new SmartTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Smart wallet"));

        TradingAgent[] tradingAgents = new TradingAgent[]{tradingAgent1, tradingAgent2};

        PortfolioManager portfolioManager = new PortfolioManager(tradingAgents);

        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        marketDataNotifier.addObserver(portfolioManager);


        List<TradingAgent> agents = new ArrayList<>();
        agents.add(tradingAgent1);
        agents.add(tradingAgent2);

        CryptoPortfolioApp.main(args, agents, marketDataNotifier);


        System.out.println();
        System.out.println("[LOG]: Start value of all wallets: " + tradingAgents.length * (defaultBalance + defaultAssetAmount * 99515.98) +
                "\n[LOG]: End value of all wallets: " + portfolioManager.getCurrentValue(90023.14f));
        //Magic numbers used for this log are the first and last close value of the data set
    }
}
