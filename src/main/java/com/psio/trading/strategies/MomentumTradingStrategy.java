package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.ArrayList;

public class MomentumTradingStrategy implements TradingStrategy {
    private final ArrayList<Float> prices = new ArrayList<>();
    private final int period;

    public MomentumTradingStrategy() {
        this.period = 10;
    }

    public MomentumTradingStrategy(int period) {
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

            double momentum = prices.getLast() - prices.getFirst();

            if (momentum > 0) {
                return TradingAction.BUY;
            }

            if (momentum < 0) {
                return TradingAction.SELL;
            }
        }

        return TradingAction.HOLD;
    }
}
