package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.ArrayList;

public class MovingAverageCrossoversTradingStrategy implements TradingStrategy {
    private final int shortPeriod;
    private final int longPeriod;

    private final ArrayList<Float> prices = new ArrayList<>();

    public MovingAverageCrossoversTradingStrategy() {
        this.shortPeriod = 100;
        this.longPeriod = 1000;
    }

    public MovingAverageCrossoversTradingStrategy(int shortPeriod, int longPeriod) {
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        //Previous prices with longPeriod amount of entries
        if (prices.size() < longPeriod) {
            prices.add(marketDataPayload.close);

        } else {
            prices.removeFirst();
            prices.add(marketDataPayload.close);

            double smaShort = calculateSMA(prices, shortPeriod);
            double smaLong = calculateSMA(prices, longPeriod);

            if (smaShort > smaLong) {
                return TradingAction.BUY;

            } else if (smaShort <= smaLong) {
                return TradingAction.SELL;

            }

        }

        return TradingAction.HOLD;
    }

    //Simple Moving Average (SMA)
    private double calculateSMA(ArrayList<Float> prices, int period) {
        int startIndex = prices.size() - period;

        double sum = 0;
        for (int i = startIndex; i < prices.size(); i++) {
            sum += prices.get(i);
        }

        return sum / period;
    }
}
