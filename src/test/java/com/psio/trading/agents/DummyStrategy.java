package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;
import com.psio.trading.strategies.TradingStrategy;

public class DummyStrategy implements TradingStrategy {
    private TradingAction action;

    public void setTradingAction(TradingAction tradingAction) {
        action = tradingAction;
    }

    public TradingAction decide(MarketDataPayload payload) {
        return action;
    }

    public void reset() {
    }
}
