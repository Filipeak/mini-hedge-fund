package com.psio.market;

import com.psio.portfolio.PortfolioManager;
import com.psio.trading.Wallet;
import com.psio.trading.agents.ConservativeTradingAgent;
import com.psio.trading.agents.TradingAgent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarketDataNotifierTest {

    @Test
    void testAddExistingObserverToListOfObservers() {
        MarketDataNotifier notifier = new MarketDataNotifier();
        ConservativeTradingAgent agent = new ConservativeTradingAgent(new Wallet(0, 0, "Conservative wallet"));
        TradingAgent[] tradingAgents = new TradingAgent[]{agent};
        PortfolioManager manager = new PortfolioManager(tradingAgents);

        notifier.addObserver(manager);
        List<MarketDataObserver> list = notifier.getObservers();
        //adding the same observer
        notifier.addObserver(manager);
        List<MarketDataObserver> listAfterAddition = notifier.getObservers();

        assertEquals(list, listAfterAddition);
    }
}