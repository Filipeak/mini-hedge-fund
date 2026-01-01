package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.BuyAndHoldTradingStrategy;
import com.psio.trading.strategies.MeanReversionTradingStrategy;
import com.psio.trading.strategies.MomentumTradingStrategy;
import com.psio.trading.strategies.VolatilityBreakoutTradingStrategy;

import java.util.ArrayList;

public class SmartTradingAgent extends TradingAgent {
    private final ArrayList<Float> prices = new ArrayList<>();


    public SmartTradingAgent(Wallet wallet) {
        super(wallet);
        this.currentStrategy = new BuyAndHoldTradingStrategy();
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        super.update(marketDataPayload);

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
//                System.out.println("Volatility regime is low");
                this.currentStrategy = new MeanReversionTradingStrategy();
            }
            if (vol > 0.005 && vol <= 0.015) {
//                System.out.println("Volatility regime is medium");
                this.currentStrategy = new MomentumTradingStrategy();

            }
            if (vol > 0.015) {
//                System.out.println("Volatility regime is high");
                this.currentStrategy = new VolatilityBreakoutTradingStrategy();
            }
        }

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
