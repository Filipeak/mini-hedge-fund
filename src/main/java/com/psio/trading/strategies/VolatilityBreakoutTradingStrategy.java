package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.ArrayList;

public class VolatilityBreakoutTradingStrategy implements TradingStrategy {
    private final ArrayList<Float> prices = new ArrayList<>();
    private final int period;

    public VolatilityBreakoutTradingStrategy() {
        this.period = 10;
    }

    public VolatilityBreakoutTradingStrategy(int period) {
        this.period = period;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        //Previous prices with period amount of entries
        if (prices.size() < period) {
            prices.add(marketDataPayload.close);
        } else {
            prices.removeFirst();
            prices.add(marketDataPayload.close);

            double rangeHigh = prices.getLast();
            double rangeLow = prices.getFirst();

            for (int i = 0; i < period; i++) {
                if (rangeHigh < prices.get(i)) {
                    rangeHigh = prices.get(i);
                }
                if (rangeLow > prices.get(i)) {
                    rangeLow = prices.get(i);
                }
            }

            if (prices.getLast() >= rangeHigh) {
                return TradingAction.BUY;
            }

            if (prices.getLast() <= rangeLow) {
                return TradingAction.SELL;
            }

        }

        return TradingAction.HOLD;
    }
}
