package com.psio.trading;

import com.psio.market.MarketDataPayload;

public class SmartTradingAgent extends TradingAgent {
    public SmartTradingAgent(Wallet wallet) {
        super(wallet);
        this.currentStrategy = new MomentumTradingStrategy(100,1000);
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        super.update(marketDataPayload);
    }

}
