package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuyAndHoldTradingStrategyTest {
    private final BuyAndHoldTradingStrategy strategy = new BuyAndHoldTradingStrategy();
    private TradingAction action;

    private MarketDataPayload marketDataPayload(float close) {
        return new MarketDataPayload(1, 1, close, 1, 1, 1);
    }

    @Test
    void testShouldBuyOnFirstPayload() {
        BuyAndHoldTradingStrategy strategy = new BuyAndHoldTradingStrategy();
        TradingAction action;

        action = strategy.decide(marketDataPayload(1));

        assertEquals(TradingAction.BUY, action);
    }

    @Test
    void testShouldSellOnProfitablePrice() {
        strategy.decide(marketDataPayload(1));
        action = strategy.decide(marketDataPayload(1.1f));

        assertEquals(TradingAction.SELL, action);
    }

    @Test
    void testShouldSellToMinimalizeLoss() {
        strategy.decide(marketDataPayload(1));
        action = strategy.decide(marketDataPayload(0.9f));

        assertEquals(TradingAction.SELL, action);
    }

    @Test
    void testShouldHoldOnSimilarPrice() {
        strategy.decide(marketDataPayload(1));
        action = strategy.decide(marketDataPayload(1.01f));

        assertEquals(TradingAction.HOLD, action);
    }
}