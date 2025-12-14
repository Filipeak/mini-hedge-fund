package com.psio.trading;

import com.psio.market.MarketDataObserver;
import com.psio.market.MarketDataPayload;

public abstract class TradingAgent implements MarketDataObserver {
    protected Wallet wallet;
    protected TradingStrategy currentStrategy;

    public TradingAgent(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {

        TradingAction decision = currentStrategy.decide(marketDataPayload);
        System.out.println(decision);

        float currentPrice = marketDataPayload.open;
        float balance = wallet.getBalance();
        float assetAmount = wallet.getAssetAmount();

        switch (decision) {

            case TradingAction.BUY:
                if (balance > 0) {
                    float amountToBuy = balance / currentPrice;
                    wallet.setBalance(balance - amountToBuy * currentPrice);
                    wallet.setAssetAmount(amountToBuy);

                    System.out.println("!!! Purchase of " + amountToBuy + " for " + currentPrice);
                }
                break;

            case TradingAction.SELL:
                if (assetAmount > 0) {
                    wallet.setAssetAmount(0);
                    wallet.setBalance(balance + assetAmount * currentPrice);

                    System.out.println("!!! Sell of " + assetAmount + " for " + currentPrice);
                }
                break;

            case TradingAction.HOLD:
                break;

        }

    }

    public float getBalance() {
        return wallet.getBalance();
    }

    public float getAssets() {
        return wallet.getAssetAmount();
    }

    public void getWalletInfo() {
        if (this instanceof ConservativeTradingAgent) System.out.println("Conservative Trading Agent: ");
        if (this instanceof SmartTradingAgent) System.out.println("Smart Trading Agent: ");

        System.out.println(
                "Current balance: " + wallet.getBalance() +
                        "\nAsset amount: " + wallet.getAssetAmount() +
                        "\nBalance + asset value: " + (wallet.getBalance() + wallet.getAssetAmount() * 90023.14)

//                Magic number 90023.14 is the last close cost of the current data
//                This method is mainly for testing
        );

    }
}
