package com.psio.trading;

import com.psio.market.MarketDataPayload;

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
