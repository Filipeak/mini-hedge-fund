package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

public class BuyAndHoldTradingStrategy implements TradingStrategy {
    private boolean hasBought = false;
    private final float takeProfitPercentage;
    private final float stopLossPercentage;
    private double buyPrice;

    public BuyAndHoldTradingStrategy() {
        this.stopLossPercentage = 0.90f;
        this.takeProfitPercentage = 1.10f;
    }

    public BuyAndHoldTradingStrategy(float takeProfitPercentage, float stopLossPercentage) {
        this.takeProfitPercentage = takeProfitPercentage;
        this.stopLossPercentage = stopLossPercentage;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        if (!hasBought) {
            hasBought = true;
            buyPrice = marketDataPayload.close;
            return TradingAction.BUY;
        }

        double currentPrice = marketDataPayload.close;

        if (currentPrice >= buyPrice * takeProfitPercentage) {
            return TradingAction.SELL;
        }

        if (currentPrice <= buyPrice * stopLossPercentage) {
            return TradingAction.SELL;
        }

        return TradingAction.HOLD;
    }
}
