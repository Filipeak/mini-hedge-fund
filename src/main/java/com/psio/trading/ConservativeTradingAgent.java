package com.psio.trading;

import com.psio.market.MarketDataPayload;

public class ConservativeTradingAgent extends TradingAgent {
    public ConservativeTradingAgent(Wallet wallet) {
        super(wallet);
        this.currentStrategy = new BuyAndHoldTradingStrategy();
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        super.update(marketDataPayload);
    }

}
