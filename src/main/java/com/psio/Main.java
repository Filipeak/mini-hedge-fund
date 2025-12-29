package com.psio;

import com.psio.market.*;
import com.psio.portfolio.*;
import com.psio.reporting.*;
import com.psio.reporting.creators.FileWriterCreator;
import com.psio.simulation.*;
import com.psio.trading.*;
import com.psio.trading.agents.*;
import com.psio.ui.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static TradingAgent[] getTradingAgents() {
        final float defaultBalance = 10000.0f;
        final float defaultAssetAmount = 0.0f;

        return new TradingAgent[]{
                new ConservativeTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "BuyAndHold wallet")),
                new SmartTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "MovingAverageCrossovers wallet")),
                new TestTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Test wallet"))
        };
    }

    // Creating a logger instance for this class
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("The application has started.");

        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            logger.error("A math error occurred: {}", e.getMessage());
        }

        logger.debug("This won't show up if the Root level is set to INFO.");


        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        TradingAgent[] tradingAgents = getTradingAgents();

        PortfolioManager portfolioManager = new PortfolioManager(tradingAgents);
        marketDataNotifier.addObserver(portfolioManager);

        PortfolioChart portfolioChart = new PortfolioChart(portfolioManager);
        new ReportCreator(portfolioManager, new FileWriterCreator("final-report.txt"));

        SimulationManager simulationManager = new SimulationManager(marketDataNotifier);
        CryptoPortfolioApp.start(args, portfolioChart, simulationManager::loadAndRunSimulation);
    }
}
