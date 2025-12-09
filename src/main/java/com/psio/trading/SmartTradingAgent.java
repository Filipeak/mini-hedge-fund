package com.psio.trading;

import com.psio.market.MarketDataPayload;

public class SmartTradingAgent extends TradingAgent {
    public SmartTradingAgent(float balance, TradingStrategy currentStrategy) {
        super(balance, currentStrategy);
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        super.update(marketDataPayload);
    }

}
