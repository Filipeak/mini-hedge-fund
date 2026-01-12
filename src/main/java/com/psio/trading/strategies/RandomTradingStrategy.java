package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;

import java.util.Random;

public class RandomTradingStrategy implements TradingStrategy {

    private final int delay;
    private final Random random = new Random();

    private int currentIndex = 0;

    public RandomTradingStrategy() {
        this.delay = 1;
    }

    public RandomTradingStrategy(int delay) {
        this.delay = delay;
    }

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        currentIndex++;

        if (currentIndex == delay) {
            currentIndex = 0;

            int strat = new Random().nextInt(3);

            return switch (strat) {
                case 0 -> TradingAction.BUY;
                case 1 -> TradingAction.SELL;
                case 2 -> TradingAction.HOLD;
                default -> throw new IllegalStateException("Unexpected value: " + strat);
            };
        } else {
            return TradingAction.BUY;
        }
    }

    @Override
    public void reset() {
    }
}
