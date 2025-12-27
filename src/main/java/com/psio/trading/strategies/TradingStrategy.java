package com.psio.trading.strategies;
import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

public interface TradingStrategy {
    TradingAction decide(MarketDataPayload marketDataPayload);
}
