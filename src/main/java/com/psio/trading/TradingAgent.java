package com.psio.trading;

import com.psio.market.MarketDataObserver;
import com.psio.market.MarketDataPayload;

public abstract class TradingAgent implements MarketDataObserver {
    protected float balance;
    protected TradingStrategy currentStrategy;

    public TradingAgent(float balance, TradingStrategy currentStrategy) {
        this.balance = balance;
        this.currentStrategy = currentStrategy;
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {

    }

    public float getBalance() {
        return balance;
    }
}
