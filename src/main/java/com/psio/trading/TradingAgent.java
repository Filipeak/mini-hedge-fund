package com.psio.trading;

import com.psio.market.MarketDataPayload;

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

    }

    public void begin() {
        this.wallet.reset();
    }

    public void end() {
        this.wallet.endInfo();

    }

    public Wallet getWallet() {
        return wallet;
    }

    public float getBalance() {
        return wallet.getBalance();
    }

    public float getAssets() {
        return wallet.getAssetAmount();
    }

}
