package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;
import com.psio.trading.strategies.TradingStrategy;
import com.psio.trading.Wallet;

public abstract class TradingAgent implements Comparable<TradingAgent> {
    protected Wallet wallet;
    protected TradingStrategy currentStrategy;

    public TradingAgent(Wallet wallet) {
        this.wallet = wallet;
    }

    public void update(MarketDataPayload marketDataPayload) {
        TradingAction decision = currentStrategy.decide(marketDataPayload);

        float currentPrice = marketDataPayload.close;
        this.wallet.updateCurrentPrice(currentPrice);

        switch (decision) {
            case TradingAction.BUY:
                wallet.tryBuyMaxAssets();
                break;

            case TradingAction.SELL:
                wallet.trySellAllAssets();
                break;

            case TradingAction.HOLD:
                break;
        }

    }

    public void begin() {
        this.wallet.reset();
    }

    public void end() {
        this.wallet.trySellAllAssets();
        this.wallet.printInfo();
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public int compareTo(TradingAgent o) {
        return (int) -(this.wallet.getCurrentValue() - o.wallet.getCurrentValue());
    }
}
