package com.psio.trading;

import com.psio.market.MarketDataPayload;

public class ConservativeTradingAgent extends TradingAgent{
    public ConservativeTradingAgent(float balance, TradingStrategy currentStrategy) {
        super(balance, currentStrategy);
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        super.update(marketDataPayload);
    }

}
