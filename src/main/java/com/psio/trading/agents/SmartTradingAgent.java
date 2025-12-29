package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.MovingAverageCrossoversTradingStrategy;

public class SmartTradingAgent extends TradingAgent {

    public SmartTradingAgent(Wallet wallet) {
        super(wallet);
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        super.update(marketDataPayload);
    }

    @Override
    public void begin() {
        super.begin();
        this.currentStrategy = new MovingAverageCrossoversTradingStrategy(100, 1000);
    }
}
