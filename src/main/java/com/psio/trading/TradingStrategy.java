package com.psio.trading;
import com.psio.market.MarketDataPayload;

public interface TradingStrategy {
    public TradingAction update(MarketDataPayload marketDataPayload);
}
