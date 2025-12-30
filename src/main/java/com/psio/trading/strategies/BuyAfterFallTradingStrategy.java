package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.ArrayList;

public class BuyAfterFallTradingStrategy implements TradingStrategy {
    private final float fallPercentage;
    private final float risePercentage;
    private final ArrayList<Float> prices = new ArrayList<>();
    private final int period;

    public BuyAfterFallTradingStrategy() {
        this.fallPercentage = 0.05f;
        this.risePercentage = 0.05f;
        this.period = 100;
    }

    public BuyAfterFallTradingStrategy(float fallPercentage, float risePercentage, int period) {
        this.fallPercentage = fallPercentage;
        this.risePercentage = risePercentage;
        this.period = period;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        //Previous prices with period amount of entries
        if (prices.size() < period) {
            prices.add(marketDataPayload.close);
        } else {
            prices.removeFirst();
            prices.add(marketDataPayload.close);

            double currentPrice = marketDataPayload.close;

            if (currentPrice <= prices.getFirst() * (1 - fallPercentage)) {
                return TradingAction.BUY;
            }

            if (currentPrice >= prices.getFirst() * (1 + risePercentage)) {
                return TradingAction.SELL;
            }
        }

        return TradingAction.HOLD;
    }

    @Override
    public void reset() {
        prices.clear();
    }
}
