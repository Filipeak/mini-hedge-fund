package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuyAfterFallTradingStrategyTest {
    private final BuyAfterFallTradingStrategy strategy = new BuyAfterFallTradingStrategy();
    private TradingAction action;

    private MarketDataPayload payload(float close) {
        return new MarketDataPayload(1, 1, close, 1, 1, 1);
    }

    @Test
    void testShouldBuyWhenCurrentPriceIsLower() {
        strategy.decide(payload(1000));
        for (int i = 0; i < 100; i++) {
            action = strategy.decide(payload(1000 - 5 * i));
        }

        assertEquals(TradingAction.BUY, action);
    }

    @Test
    void testShouldSellWhenCurrentPriceIsHigher() {
        strategy.decide(payload(1000));
        for (int i = 0; i < 100; i++) {
            action = strategy.decide(payload(1000 + 5 * i));
        }

        assertEquals(TradingAction.SELL, action);
    }

    @Test
    void testShouldHoldWhenPriceIsSimilar() {
        strategy.decide(payload(1000));
        for (int i = 0; i < 100; i++) {
            action = strategy.decide(payload(1000 - i * 0.1f));
        }
        assertEquals(TradingAction.HOLD, action);
    }
}