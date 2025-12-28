package com.psio.reporting;

import com.psio.market.MarketDataPayload;
import com.psio.portfolio.PortfolioManager;
import com.psio.reporting.creators.StringWriterCreator;
import com.psio.trading.Wallet;
import com.psio.trading.agents.ConservativeTradingAgent;
import com.psio.trading.agents.SmartTradingAgent;
import com.psio.trading.agents.TradingAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportCreatorTest {

    private StringWriterCreator reportResult;
    PortfolioManager portfolioManager;

    @BeforeEach
    void setUp() {
        final float defaultBalance = 10000.0f;
        final float defaultAssetAmount = 0.0f;

        TradingAgent[] tradingAgents = new TradingAgent[]{
                new ConservativeTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Conservative wallet")),
                new SmartTradingAgent(new Wallet(defaultBalance, defaultAssetAmount, "Smart wallet")),
        };

        portfolioManager = new PortfolioManager(tradingAgents);
        reportResult = new StringWriterCreator();
        new ReportCreator(portfolioManager, reportResult);
    }

    @Test
    void testReportCreator() {
        // run simulation
        portfolioManager.begin();
        portfolioManager.update(new MarketDataPayload(1, 1, 1, 1, 1, 1));
        portfolioManager.end();

        String result = reportResult.getData();
        assertTrue(result.contains("Finalny Balans"));
        assertTrue(result.contains("Zwrot (ROR)"));
        assertTrue(result.contains("Max Drawdown"));
        assertTrue(result.contains("Win Rate"));
    }
}
