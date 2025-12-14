package com.psio.trading;

import com.psio.market.MarketDataPayload;

import java.util.ArrayList;

public class MomentumTradingStrategy implements TradingStrategy {
    //Used Strategy = Moving Average Crossovers
    private final int SHORT_PERIOD;
    private final int LONG_PERIOD;

    private final ArrayList<MarketDataPayload> marketHistory = new ArrayList<>();

    public MomentumTradingStrategy(int SHORT_PERIOD, int LONG_PERIOD) {
        this.SHORT_PERIOD = SHORT_PERIOD;
        this.LONG_PERIOD = LONG_PERIOD;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {

        //Market history data for LONG_PERIOD of entries
        if (marketHistory.size() < LONG_PERIOD) {
            marketHistory.add(marketDataPayload);

        } else {
            marketHistory.removeFirst();
            marketHistory.add(marketDataPayload);

            double smaShort = calculateSMA(marketHistory, SHORT_PERIOD);
            double smaLong = calculateSMA(marketHistory, LONG_PERIOD);

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
