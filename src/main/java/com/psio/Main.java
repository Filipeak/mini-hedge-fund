package com.psio;

import com.psio.market.*;
import com.psio.portfolio.*;
import com.psio.reporting.*;
import com.psio.reporting.creators.FileWriterCreator;
import com.psio.simulation.*;
import com.psio.trading.*;
import com.psio.trading.agents.*;
import com.psio.ui.*;

public class Main {
    public static void main(String[] args) {
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        final float defaultBalance = 10000.0f;
        final float defaultAssetAmount = 0.0f;

        TradingAgent[] tradingAgents = new TradingAgent[]{
                new ConservativeTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Conservative wallet")),
                new SmartTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Smart wallet")),
        };

        PortfolioManager portfolioManager = new PortfolioManager(tradingAgents);
        marketDataNotifier.addObserver(portfolioManager);

        PortfolioChart portfolioChart = new PortfolioChart(portfolioManager);
        new ReportCreator(portfolioManager, new FileWriterCreator("final-report.txt"));

        SimulationManager simulationManager = new SimulationManager(marketDataNotifier);
        CryptoPortfolioApp.start(args, portfolioChart, simulationManager::loadAndRunSimulation);
    }
}