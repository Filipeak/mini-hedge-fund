package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

public class BuyAfterFallTradingStrategy implements TradingStrategy {
    private final float fallPercentage;
    private final float risePercentage;
    private double lastBuyPrice;
    private double lastSellPrice = 10000000;
    private boolean hasAssets = false;

    public BuyAfterFallTradingStrategy() {
        this.fallPercentage = 0.05f;
        this.risePercentage = 0.05f;
    }

    public BuyAfterFallTradingStrategy(float fallPercentage, float risePercentage) {
        this.fallPercentage = fallPercentage;
        this.risePercentage = risePercentage;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        double currentPrice = marketDataPayload.close;

        if (currentPrice <= lastSellPrice * (1 - fallPercentage) && !hasAssets) {
            lastBuyPrice = currentPrice;
            hasAssets = true;
            return TradingAction.BUY;
        }

        if (currentPrice >= lastBuyPrice * (1 + risePercentage) && hasAssets) {
            lastSellPrice = currentPrice;
            hasAssets = false;
            return TradingAction.SELL;
        }

        return TradingAction.HOLD;
    }
}
