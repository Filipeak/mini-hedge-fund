package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.ArrayList;

public class MomentumTradingStrategy implements TradingStrategy {
    //Used Strategy = Moving Average Crossovers
    private final int shortPeriod;
    private final int longPeriod;

    private final ArrayList<MarketDataPayload> marketHistory = new ArrayList<>();

    public MomentumTradingStrategy(int shortPeriod, int longPeriod) {
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        //Market history data for longPeriod of entries
        if (marketHistory.size() < longPeriod) {
            marketHistory.add(marketDataPayload);

        } else {
            marketHistory.removeFirst();
            marketHistory.add(marketDataPayload);

            double smaShort = calculateSMA(marketHistory, shortPeriod);
            double smaLong = calculateSMA(marketHistory, longPeriod);

            if (smaShort > smaLong) {
                return TradingAction.BUY;

            } else if (smaShort <= smaLong) {
                return TradingAction.SELL;

            }

        }

        return TradingAction.HOLD;
    }

    //Simple Moving Average (SMA)
    private double calculateSMA(ArrayList<MarketDataPayload> history, int period) {
        int startIndex = history.size() - period;

        double sum = 0;
        for (int i = startIndex; i < history.size(); i++) {
            sum += history.get(i).close;
        }

        return sum / period;
    }
}
