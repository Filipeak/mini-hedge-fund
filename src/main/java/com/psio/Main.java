package com.psio;

import com.psio.files.FileReportSaver;
import com.psio.market.MarketDataNotifier;
import com.psio.reporting.ReportCreator;
import com.psio.simulation.SimulationManager;
import com.psio.trading.*;
import com.psio.ui.CryptoPortfolioApp;
import com.psio.ui.PortfolioChart;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Notifier
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        // Agents
        TradingAgent tradingAgent1 = new ConservativeTradingAgent(new Wallet(10000.0f, 0.0f));
        TradingAgent tradingAgent2 = new SmartTradingAgent(new Wallet(10000.0f, 0.0f));

        marketDataNotifier.addObserver(tradingAgent1);
        marketDataNotifier.addObserver(tradingAgent2);

        List<TradingAgent> agents = new ArrayList<>();
        agents.add(tradingAgent1);
        agents.add(tradingAgent2);

        // Portfolio Chart
        PortfolioChart portfolioChart = new PortfolioChart(agents);
        marketDataNotifier.addPortfolioObserver(portfolioChart); // TODO change to use PortfolioManager

        // Report Creator
        ReportCreator reportCreator = new ReportCreator(
                agents,
                new FileReportSaver("final-report.txt"));
        marketDataNotifier.addPortfolioObserver(reportCreator); // TODO change to use PortfolioManager

        // Application start
        SimulationManager simulationManager = new SimulationManager(marketDataNotifier);
        CryptoPortfolioApp.start(args, portfolioChart, simulationManager::loadAndRunSimulation);
    }
}