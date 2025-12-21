package com.psio.trading;

import com.psio.market.MarketDataPayload;

public interface PortfolioObserver {
    void onBegin();
    void onEnd();
    void onChange(MarketDataPayload marketDataPayload);
}
