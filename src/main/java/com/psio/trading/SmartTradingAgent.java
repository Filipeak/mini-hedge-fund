package com.psio.trading;

import com.psio.market.MarketDataPayload;

public class SmartTradingAgent extends TradingAgent {
    public SmartTradingAgent(Wallet wallet) {
        super(wallet);
        this.currentStrategy = new MomentumTradingStrategy(100,1000);
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        System.out.println("SmartTradingAgent update for " + marketDataPayload.timestamp + ": ");
        super.update(marketDataPayload);
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

}
