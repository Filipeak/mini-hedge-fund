package com.psio;

import com.psio.market.*;
import com.psio.portfolio.*;
import com.psio.reporting.*;
import com.psio.reporting.creators.FileWriterCreator;
import com.psio.simulation.*;
import com.psio.trading.*;
import com.psio.trading.agents.*;
import com.psio.trading.strategies.*;
import com.psio.ui.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private static TradingAgent[] getTradingAgents() {
        final float defaultBalance = 10000.0f;
        final float defaultAssetAmount = 0.0f;

        return new TradingAgent[]{
                new ConservativeTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "BuyAfterFall wallet"),
                        new BuyAfterFallTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "BuyAndHold wallet"),
                        new BuyAndHoldTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "MeanReversion wallet"),
                        new MeanReversionTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "Momentum wallet"),
                        new MomentumTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "MovingAverage wallet"),
                        new MovingAverageCrossoversTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "RelativeStrengthIndex wallet"),
                        new RelativeStrengthIndexTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "VolatilityBreakout wallet"),
                        new VolatilityBreakoutTradingStrategy()
                ),
                new SmartTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "Smart wallet")
                ),
                new ConservativeTradingAgent(
                        new Wallet(defaultBalance, defaultAssetAmount, "ML wallet"),
                        new MLTradingStrategy()
                )
        };
    }

    public static void main(String[] args) {
        logger.info("The application has started.");

        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        TradingAgent[] tradingAgents = getTradingAgents();

        PortfolioManager portfolioManager = new PortfolioManager(tradingAgents);
        marketDataNotifier.addObserver(portfolioManager);

        PortfolioChart portfolioChart = new PortfolioChart(portfolioManager);
        new ReportCreator(portfolioManager, new FileWriterCreator("final-report.txt"));

        SimulationManager simulationManager = new SimulationManager(marketDataNotifier);
        CryptoPortfolioApp.start(args, portfolioChart, simulationManager::loadAndRunSimulation);

        logger.info("The application has finished.");
    }
}
