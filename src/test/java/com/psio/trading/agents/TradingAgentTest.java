package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;
import com.psio.trading.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradingAgentTest {
    private ConservativeTradingAgent agent;
    private DummyStrategy strategy;
    private MarketDataPayload payload;
    private final float currentPrice = 5;

    @BeforeEach
    void setUp() {
        Wallet wallet = new Wallet(100, 10, "test");
        strategy = new DummyStrategy();
        agent = new ConservativeTradingAgent(wallet, strategy);
        wallet.updateCurrentPrice(currentPrice);
        //payload is only for initialization of update method
        payload = new MarketDataPayload(1, 1, 1, 1, 1, 1);
    }

    @Test
    void testBuyAssetsWhenStrategySaysBuy() {
        strategy.setTradingAction(TradingAction.BUY);
        agent.update(payload);

        assertEquals(0, agent.getWallet().getBalance());
    }

    @Test
    void testSellAssetsWhenStrategySaysSell() {
        strategy.setTradingAction(TradingAction.SELL);
        agent.update(payload);

        assertEquals(0, agent.getWallet().getAssetAmount());
    }

    @Test
    void testDoNothingWhenStrategySaysHold() {
        float defaultBalance = agent.getWallet().getBalance();
        float defaultAssetAmount = agent.getWallet().getAssetAmount();

        strategy.setTradingAction(TradingAction.HOLD);
        agent.update(payload);

        assertEquals(defaultBalance, agent.getWallet().getBalance());
        assertEquals(defaultAssetAmount, agent.getWallet().getAssetAmount());
    }

    @Test
    void testIsEndMethodWorks() {
        float endBalance = agent.getWallet().getBalance() + agent.getWallet().getAssetAmount() * currentPrice;

        agent.end();

        assertEquals(endBalance, agent.getWallet().getBalance());
        assertEquals(0, agent.getWallet().getAssetAmount());
    }
}