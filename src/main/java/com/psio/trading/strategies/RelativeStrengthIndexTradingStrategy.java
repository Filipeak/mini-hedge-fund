package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.ArrayList;

public class RelativeStrengthIndexTradingStrategy implements TradingStrategy {
    private final ArrayList<Float> prices = new ArrayList<>();
    private final double buyThreshold;
    private final double sellThreshold;
    private final int period;

    public RelativeStrengthIndexTradingStrategy() {
        this.period = 14;
        this.buyThreshold = 30;
        this.sellThreshold = 70;
    }

    public RelativeStrengthIndexTradingStrategy(int period) {
        this.period = period;
        this.buyThreshold = 30;
        this.sellThreshold = 70;
    }

    public RelativeStrengthIndexTradingStrategy(int period, double buyThreshold, double sellThreshold) {
        this.period = period;
        this.buyThreshold = buyThreshold;
        this.sellThreshold = sellThreshold;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        //Previous prices with period amount of entries
        if (prices.size() < period) {
            prices.add(marketDataPayload.close);
        } else {
            prices.removeFirst();
            prices.add(marketDataPayload.close);

            double avgGain = 0;
            double avgLoss = 0;

            for (int i = 1; i < prices.size(); i++) {
                double change = prices.get(i) - prices.get(i - 1);
                if (change > 0) avgGain += change;
                else avgLoss -= change;
            }

            avgGain = avgGain / period;
            avgLoss = avgLoss / period;

            double rsi = 100 - (100 / (1 + avgGain / avgLoss));

            if (rsi < buyThreshold) {
                return TradingAction.BUY;
            }

            if (rsi > sellThreshold) {
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
