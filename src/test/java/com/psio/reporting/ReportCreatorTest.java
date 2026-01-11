package com.psio.reporting;

import com.psio.market.MarketDataNotifier;
import com.psio.market.MarketDataPayload;
import com.psio.portfolio.PortfolioManager;
import com.psio.reporting.creators.StringWriterCreator;
import com.psio.simulation.SimulationManager;
import com.psio.trading.Wallet;
import com.psio.trading.agents.ConservativeTradingAgent;
import com.psio.trading.agents.TradingAgent;
import com.psio.trading.strategies.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ReportCreatorTest {

    private StringWriterCreator reportResult;
    private PortfolioManager portfolioManager;
    private SimulationManager simulationManager;

    private final String CSV_DATA_FILE = "src/main/resources/data.csv";

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
                )
        };
    }

    @BeforeEach
    void setUp() {
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        TradingAgent[] tradingAgents = getTradingAgents();

        portfolioManager = new PortfolioManager(tradingAgents);
        marketDataNotifier.addObserver(portfolioManager);

        reportResult = new StringWriterCreator();
        new ReportCreator(portfolioManager, reportResult);

        simulationManager = new SimulationManager(marketDataNotifier);
    }

    @Test
    void testReportCreator() {
        // run simulation
        portfolioManager.begin();
        portfolioManager.update(new MarketDataPayload(1, 1, 1, 1, 1, 1));
        portfolioManager.end();

        String result = reportResult.getData();
        System.out.println(result);
        assertTrue(result.contains("Finalny Balans:   70000.00 PLN"));
        assertTrue(result.contains("Zwrot (ROR):      0.00 %"));
        assertTrue(result.contains("Max Drawdown:     0.00 %"));
        assertTrue(result.contains("Win Rate:         0.00 %"));
    }

    @Test
    void testReportWithRealCsvFile() {
        File csvFile = new File(CSV_DATA_FILE);
        simulationManager.loadAndRunSimulation(csvFile);

        String result = reportResult.getData();
        System.out.println(result);
        assertTrue(result.contains("Finalny Balans:   77162.25 PLN"));
        assertTrue(result.contains("Zwrot (ROR):      10.23 %"));
        assertTrue(result.contains("Max Drawdown:     -16.28 %"));
        assertTrue(result.contains("Win Rate:         41.08 %"));
    }
}