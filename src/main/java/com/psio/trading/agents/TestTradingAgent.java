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
//        this.currentStrategy = new MomentumTradingStrategy(10);
//        this.currentStrategy = new VolatilityBreakoutTradingStrategy(10);
//        this.currentStrategy = new MeanReversionTradingStrategy(15, 2.0);
        this.currentStrategy = new RelativeStrengthIndexTradingStrategy(14, 30, 70);
    }
}


