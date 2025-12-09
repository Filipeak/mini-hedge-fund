package com.psio.market;

public interface MarketDataObserver {
    public void update(MarketDataPayload marketDataPayload);
}
