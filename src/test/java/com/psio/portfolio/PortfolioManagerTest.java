package com.psio.portfolio;

import com.psio.reporting.ReportCreator;
import com.psio.reporting.creators.StringWriterCreator;
import com.psio.trading.Wallet;
import com.psio.trading.agents.ConservativeTradingAgent;
import com.psio.trading.agents.TradingAgent;
import com.psio.trading.strategies.BuyAndHoldTradingStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioManagerTest {

    @Test
    void testAddExistingObserverToListOfObservers() {
        ConservativeTradingAgent agent = new ConservativeTradingAgent(
                new Wallet(0, 0, "Conservative wallet"),
                new BuyAndHoldTradingStrategy()
        );

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
}