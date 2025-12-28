package com.psio.reporting;

import com.psio.market.MarketDataNotifier;
import com.psio.market.MarketDataPayload;
import com.psio.portfolio.PortfolioManager;
import com.psio.reporting.creators.StringWriterCreator;
import com.psio.simulation.SimulationManager;
import com.psio.trading.Wallet;
import com.psio.trading.agents.ConservativeTradingAgent;
import com.psio.trading.agents.SmartTradingAgent;
import com.psio.trading.agents.TradingAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ReportCreatorTest {

    private StringWriterCreator reportResult;
    private PortfolioManager portfolioManager;
    private SimulationManager simulationManager;

    private final String CSV_DATA_FILE = "src/main/resources/data.csv";

    @BeforeEach
    void setUp() {
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();

        final float defaultBalance = 10000.0f;
        final float defaultAssetAmount = 0.0f;

        TradingAgent[] tradingAgents = new TradingAgent[]{
                new ConservativeTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Conservative wallet")),
                new SmartTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Smart wallet")),
        };

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
        assertTrue(result.contains("Finalny Balans:   20000,00 PLN"));
        assertTrue(result.contains("Zwrot (ROR):      0,00 %"));
        assertTrue(result.contains("Max Drawdown:     0,00 %"));
        assertTrue(result.contains("Win Rate:         0,00 %"));
        System.out.println(result);
    }

    @Test
    void testReportWithRealCsvFile() {
        File csvFile = new File(CSV_DATA_FILE);
        simulationManager.doLoadAndRunSimulation(csvFile);

        String result = reportResult.getData();
        assertTrue(result.contains("Finalny Balans:   20509,76 PLN"));
        assertTrue(result.contains("Zwrot (ROR):      2,55 %"));
        assertTrue(result.contains("Max Drawdown:     -22,26 %"));
        assertTrue(result.contains("Win Rate:         33,33 %"));
    }
}