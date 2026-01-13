package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.BuyAfterFallTradingStrategy;
import com.psio.trading.strategies.BuyAndHoldTradingStrategy;
import com.psio.trading.strategies.RelativeStrengthIndexTradingStrategy;
import com.psio.trading.strategies.VolatilityBreakoutTradingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SmartTradingAgentTest {
    private SmartTradingAgent agent;

    @BeforeEach
    void setUp() {
        Wallet wallet = new Wallet(100, 10, "test");
        agent = new SmartTradingAgent(wallet);
    }

    @Test
    void testIsStartingStrategyBuyAndHold() {
        agent.begin();

        assertInstanceOf(BuyAndHoldTradingStrategy.class, agent.currentStrategy);
    }

    @Test
    void testIsCurrentStrategyBuyAfterFallWhenVolatilityIsReallySmall() {
        setVolatilityToAgent(agent, 0.001f);
        agent.update(new MarketDataPayload(1, 1, 100, 1, 1, 1));

        assertInstanceOf(BuyAfterFallTradingStrategy.class, agent.currentStrategy);
    }

    @Test
    void testIsCurrentStrategyRelativeStrengthIndexWhenVolatilityIsMedium() {
        setVolatilityToAgent(agent, 0.01f);
        agent.update(new MarketDataPayload(1, 1, 100, 1, 1, 1));

        assertInstanceOf(RelativeStrengthIndexTradingStrategy.class, agent.currentStrategy);
    }

    @Test
    void testIsCurrentStrategyVolatilityBreakoutWhenVolatilityIsHigh() {
        setVolatilityToAgent(agent, 0.02f);
        agent.update(new MarketDataPayload(1, 1, 100, 1, 1, 1));

        assertInstanceOf(VolatilityBreakoutTradingStrategy.class, agent.currentStrategy);
    }

    private void setVolatilityToAgent(SmartTradingAgent agent, float expectedVolatility) {
        float basePrice = 100;
        for (int i = 0; i < 100; i++) {
            float price;
            if (i % 2 == 0) {
                price = basePrice;
            } else {
                price = basePrice * (1.0f + expectedVolatility);
            }
            agent.update(new MarketDataPayload(1, 1, price, 1, 1, 1));
        }
    }
}