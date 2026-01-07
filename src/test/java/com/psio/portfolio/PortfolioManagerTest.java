package com.psio.portfolio;

import com.psio.reporting.ReportCreator;
import com.psio.reporting.creators.StringWriterCreator;
import com.psio.trading.Wallet;
import com.psio.trading.agents.ConservativeTradingAgent;
import com.psio.trading.agents.SmartTradingAgent;
import com.psio.trading.agents.TestTradingAgent;
import com.psio.trading.agents.TradingAgent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioManagerTest {

    @Test
    void testAddExistingObserverToListOfObservers() {
        ConservativeTradingAgent agent = new ConservativeTradingAgent(new Wallet(0, 0, "Conservative wallet"));

        TradingAgent[] tradingAgents = new TradingAgent[]{agent};

        PortfolioManager manager = new PortfolioManager(tradingAgents);
        StringWriterCreator writerCreator = new StringWriterCreator();
        ReportCreator creator = new ReportCreator(manager, writerCreator);

        manager.addObserver(creator);
        List<PortfolioObserver> list = manager.getObservers();
        //adding the same observer
        manager.addObserver(creator);
        List<PortfolioObserver> listAfterAddition = manager.getObservers();

        assertEquals(list, listAfterAddition);
    }

    @Test
    void testTradingAgentSorting() {
        TradingAgent[] tradingAgents = new TradingAgent[]{
                new ConservativeTradingAgent(new Wallet(100, 0, "BuyAndHold wallet")),
                new SmartTradingAgent(new Wallet(200, 0, "MovingAverageCrossovers wallet")),
                new TestTradingAgent(new Wallet(0, 0, "Test wallet"))
        };

        TradingAgent[] sortedTradingAgents = new TradingAgent[]{
                new SmartTradingAgent(new Wallet(200, 0, "MovingAverageCrossovers wallet")),
                new ConservativeTradingAgent(new Wallet(100, 0, "BuyAndHold wallet")),
                new TestTradingAgent(new Wallet(0, 0, "Test wallet"))
        };

        PortfolioManager portfolioManager = new PortfolioManager(tradingAgents);

        portfolioManager.end();

        for (int i = 0; i < portfolioManager.getAgents().length; i++) {
            assertEquals(portfolioManager.getAgents()[i].getWallet().getName(), sortedTradingAgents[i].getWallet().getName());
        }
    }
}