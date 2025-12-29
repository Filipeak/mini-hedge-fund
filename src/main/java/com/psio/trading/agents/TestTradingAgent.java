package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.*;

public class TestTradingAgent extends TradingAgent {

    public TestTradingAgent(Wallet wallet) {
        super(wallet);
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        super.update(marketDataPayload);
    }

    @Override
    public void begin() {
        super.begin();
//        this.currentStrategy = new MomentumTradingStrategy();
//        this.currentStrategy = new VolatilityBreakoutTradingStrategy();
//        this.currentStrategy = new MeanReversionTradingStrategy();
//        this.currentStrategy = new RelativeStrengthIndexTradingStrategy();
        this.currentStrategy = new BuyAfterFallTradingStrategy();
    }
}
