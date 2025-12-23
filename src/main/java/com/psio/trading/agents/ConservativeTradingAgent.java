package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.BuyAndHoldTradingStrategy;

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
