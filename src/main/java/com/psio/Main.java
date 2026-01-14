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

    private static Wallet walletCreator(String name) {
        final float defaultBalance = 10000.0f;
        final float defaultAssetAmount = 0.0f;
        return new Wallet(defaultBalance, defaultAssetAmount, name + " wallet");
    }

    private static TradingAgent[] getTradingAgents() {
        return new TradingAgent[]{
                new ConservativeTradingAgent(
                        walletCreator("BuyAfterFall"),
                        new BuyAfterFallTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        walletCreator("BuyAndHold"),
                        new BuyAndHoldTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        walletCreator("MeanReversion"),
                        new MeanReversionTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        walletCreator("Momentum"),
                        new MomentumTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        walletCreator("MovingAverage"),
                        new MovingAverageCrossoversTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        walletCreator("RelativeStrengthIndex"),
                        new RelativeStrengthIndexTradingStrategy()
                ),
                new CautiousTradingAgent(
                        walletCreator("CautiousRSI"),
                        new RelativeStrengthIndexTradingStrategy()
                ),
                new AdaptiveRSITradingAgent(
                        walletCreator("AdaptiveRSI")
                ),
                new ConservativeTradingAgent(
                        walletCreator("VolatilityBreakout"),
                        new VolatilityBreakoutTradingStrategy()
                ),
                new SmartTradingAgent(
                        walletCreator("Smart")
                ),
                new ConservativeTradingAgent(
                        walletCreator("Random"),
                        new RandomTradingStrategy()
                ),
                new ConservativeTradingAgent(
                        walletCreator("ML"),
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
