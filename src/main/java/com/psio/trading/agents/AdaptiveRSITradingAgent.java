package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.*;

import java.util.ArrayList;

public class AdaptiveRSITradingAgent extends TradingAgent {
    private final ArrayList<Float> prices = new ArrayList<>();

    private final TradingStrategy[] timeframes = new TradingStrategy[]{
            new RelativeStrengthIndexTradingStrategy(21),
            new RelativeStrengthIndexTradingStrategy(14)
    };

    public AdaptiveRSITradingAgent(Wallet wallet) {
        super(wallet);
        this.currentStrategy = new BuyAndHoldTradingStrategy();
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        int window = 300;

        if (prices.size() >= window) {
            prices.removeFirst();
        }

        prices.add(marketDataPayload.close);

        int minWindow = 100;

        if (prices.size() >= minWindow) {
            boolean isStrongUptrend = checkTrend(prices);

            if (isStrongUptrend) {
                this.currentStrategy = timeframes[0];
                timeframes[1].decide(marketDataPayload);
            } else {
                timeframes[0].decide(marketDataPayload);
                this.currentStrategy = timeframes[1];
            }
        }

        super.update(marketDataPayload);
    }

    @Override
    public void begin() {
        super.begin();
        this.currentStrategy = new BuyAndHoldTradingStrategy();
    }

    private boolean checkTrend(ArrayList<Float> prices) {
        double sum = 0;
        for (int i = 0; i < prices.size(); i++) {
            sum += prices.get(prices.size() - 1 - i);
        }
        double sma = sum / prices.size();

        double difference = Math.abs(prices.getLast() - sma) / sma;

        double trendThreshold = 0.03;

        return difference > trendThreshold;
    }
}
