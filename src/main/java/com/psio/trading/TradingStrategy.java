package com.psio.trading;
import com.psio.market.MarketDataPayload;

public interface TradingStrategy {
    TradingAction decide(MarketDataPayload marketDataPayload);
}
