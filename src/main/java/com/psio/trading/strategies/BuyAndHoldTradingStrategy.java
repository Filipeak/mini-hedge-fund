package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

public class BuyAndHoldTradingStrategy implements TradingStrategy {
    private boolean hasBought = false;

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {

        if (hasBought)
            return TradingAction.HOLD;


        hasBought = true;
        return TradingAction.BUY;

    }
}
