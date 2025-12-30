package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.TradingStrategy;

public class SmartTradingAgent extends TradingAgent {

    public SmartTradingAgent(Wallet wallet, TradingStrategy currentStrategy) {
        super(wallet, currentStrategy);
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        super.update(marketDataPayload);
    }

    @Override
    public void begin() {
        super.begin();
        this.currentStrategy.reset();
        //Currently identical to ConservativeTradingAgent will change soon (temp comment)
    }
}
