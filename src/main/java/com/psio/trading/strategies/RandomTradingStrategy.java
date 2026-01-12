package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.Random;

public class RandomTradingStrategy implements TradingStrategy {

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        int strat = new Random().nextInt(3);

        return switch (strat) {
            case 0 -> TradingAction.BUY;
            case 1 -> TradingAction.SELL;
            case 2 -> TradingAction.HOLD;
            default -> throw new IllegalStateException("Unexpected value: " + strat);
        };
    }

    @Override
    public void reset() {
    }
}
