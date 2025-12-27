package com.psio.market;

public interface MarketDataObserver {

    void update(MarketDataPayload marketDataPayload);

    void begin();

    void end();
}
