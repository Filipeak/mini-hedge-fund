package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;
import com.psio.trading.strategies.TradingStrategy;
import com.psio.trading.Wallet;

public abstract class TradingAgent {
    protected Wallet wallet;
    protected TradingStrategy currentStrategy;

    public TradingAgent(Wallet wallet) {
        this.wallet = wallet;
    }

    public void update(MarketDataPayload marketDataPayload) {
        TradingAction decision = currentStrategy.decide(marketDataPayload);

        float currentPrice = marketDataPayload.close;

        switch (decision) {
            case TradingAction.BUY:
                wallet.tryBuyMaxAssets(currentPrice);
                break;

            case TradingAction.SELL:
                wallet.trySellAllAssets(currentPrice);
                break;

            case TradingAction.HOLD:
                break;
        }

        wallet.updateCurrentPrice(currentPrice);
    }

    public void begin() {
        this.wallet.reset();
    }

    public void end() {
        this.wallet.printInfo();
    }

    public Wallet getWallet() {
        return wallet;
    }
}
