package com.psio.trading;

import com.psio.market.MarketDataPayload;

public class ConservativeTradingAgent extends TradingAgent {
    public ConservativeTradingAgent(Wallet wallet) {
        super(wallet);
        this.currentStrategy = new BuyAndHoldTradingStrategy();
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        System.out.println("ConservativeTradingAgent update for " + marketDataPayload.timestamp + ": ");
        super.update(marketDataPayload);
    }

}
