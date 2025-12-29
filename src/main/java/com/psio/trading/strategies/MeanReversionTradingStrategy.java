package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.ArrayList;

public class MeanReversionTradingStrategy implements TradingStrategy {
    //Mean Reversion (Z-Score Strategy)
    private final ArrayList<Float> prices = new ArrayList<>();
    private final int period;
    private final double entryThreshold;

    public MeanReversionTradingStrategy() {
        this.period = 15;
        this.entryThreshold = 2.0;
    }

    public MeanReversionTradingStrategy(int period, double entryThreshold) {
        this.period = period;
        this.entryThreshold = entryThreshold;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        //Previous prices with period amount of entries
        if (prices.size() < period) {
            prices.add(marketDataPayload.close);
        } else {
            prices.removeFirst();
            prices.add(marketDataPayload.close);

            double mean = 0.0;
            for (Float price : prices) mean += price;
            mean /= prices.size();

            double z = (prices.getLast() - mean) / stdDev(mean);

            if (z > entryThreshold) {
                return TradingAction.SELL;
            }

            if (z < -entryThreshold) {
                return TradingAction.BUY;
            }

        }

        return TradingAction.HOLD;
    }

    private double stdDev(double mean) {
        double variance = 0.0;
        for (double price : prices) {
            variance += (price - mean) * (price - mean);
        }
        variance /= prices.size();

        return Math.sqrt(variance);
    }
}
