package com.psio.trading;

import com.psio.market.MarketDataPayload;

public abstract class TradingAgent{
    protected Wallet wallet;
    protected TradingStrategy currentStrategy;

    public TradingAgent(Wallet wallet) {
        this.wallet = wallet;
    }

    public void update(MarketDataPayload marketDataPayload) {

        TradingAction decision = currentStrategy.decide(marketDataPayload);

        float currentPrice = marketDataPayload.open;

        switch (decision) {
            case TradingAction.BUY:
                System.out.println(decision);
                wallet.buyAssets(currentPrice);
                break;

            case TradingAction.SELL:
                System.out.println(decision);
                wallet.sellAssets(currentPrice);
                break;

            case TradingAction.HOLD:
                break;

        }

    }

    public void begin() {

    }

    public void end() {

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
