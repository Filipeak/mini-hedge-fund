package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.*;

import java.util.ArrayList;

public class SmartTradingAgent extends TradingAgent {
    private final ArrayList<Float> prices = new ArrayList<>();

    TradingStrategy[] strategies = new TradingStrategy[]{
            new BuyAfterFallTradingStrategy(),
            new RelativeStrengthIndexTradingStrategy(),
            new VolatilityBreakoutTradingStrategy()
    };

    public SmartTradingAgent(Wallet wallet) {
        super(wallet);
        this.currentStrategy = new BuyAndHoldTradingStrategy();
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {

        int window = 720;
        if (prices.size() < window) {
            prices.add(marketDataPayload.close);
        } else {
            prices.removeFirst();
            prices.add(marketDataPayload.close);
        }

        int minWindow = 100;
        if (prices.size() >= minWindow) {

            double vol = calculateVolatility(prices);

            if (vol <= 0.005) {
                this.currentStrategy = strategies[0];
                strategies[1].decide(marketDataPayload);
                strategies[2].decide(marketDataPayload);
            }
            if (vol > 0.005 && vol <= 0.015) {
                strategies[0].decide(marketDataPayload);
                this.currentStrategy = strategies[1];
                strategies[2].decide(marketDataPayload);

            }
            if (vol > 0.015) {
                strategies[0].decide(marketDataPayload);
                strategies[1].decide(marketDataPayload);
                this.currentStrategy = strategies[2];
            }
        }

        super.update(marketDataPayload);
    }

    @Override
    public void begin() {
        super.begin();
        this.currentStrategy = new BuyAndHoldTradingStrategy();
    }

    public static double calculateVolatility(ArrayList<Float> prices) {
        ArrayList<Double> returns = new ArrayList<>();

        for (int i = 0; i < prices.size() - 1; i++) {
            returns.add(Math.log(prices.get(i + 1) / prices.get(i)));
        }

        double mean = 0.0;
        for (double r : returns) mean += r;
        mean /= returns.size();

        double variance = 0.0;
        for (double r : returns) {
            variance += (r - mean) * (r - mean);
        }
        variance /= returns.size();

        return Math.sqrt(variance);
    }

}
