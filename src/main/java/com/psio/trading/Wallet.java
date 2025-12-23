package com.psio.trading;

public class Wallet {
    private final float defaultBalance;
    private final float defaultAssetAmount;
    private final String name;

    private float balance;
    private float assetAmount;
    private int transactionCount = 0;
    private int transactionWinCount = 0;
    private float transactionBuyPrice;

    public Wallet(float defaultBalance, float defaultAssetAmount, String name) {
        this.defaultBalance = defaultBalance;
        this.defaultAssetAmount = defaultAssetAmount;
        this.name = name;

        reset();
    }

    public void reset() {
        this.balance = defaultBalance;
        this.assetAmount = defaultAssetAmount;
    }

    public void printInfo() {
        System.out.println("\nWallet " + name
                + "\nBalance: " + balance
                + "\nAsset amount: " + assetAmount
                + "\nTransaction count: " + transactionCount
                + "\nTransactions won: " + transactionWinCount);
    }

    public float getBalance() {
        return balance;
    }

    public float getAssetAmount() {
        return assetAmount;
    }

    public float getCurrentValue(float currentPrice) {
        return balance + assetAmount * currentPrice;
    }

    public void tryBuyMaxAssets(float currentPrice) {
        if (balance > 0) {
            System.out.println("[" + name + " LOG]: Purchase of " + balance / currentPrice + " for " + currentPrice);

            this.assetAmount = balance / currentPrice;
            this.balance = 0;
            this.transactionBuyPrice = currentPrice;
        }
    }

    public void trySellAllAssets(float currentPrice) {
        if (assetAmount > 0) {
            System.out.println("[" + name + " LOG]: Sell of " + assetAmount + " for " + currentPrice);

            this.transactionCount++;
            this.balance = balance + assetAmount * currentPrice;
            this.assetAmount = 0;

            if (currentPrice > transactionBuyPrice) {
                this.transactionWinCount++;
            }
        }
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public int getTransactionWinCount() {
        return transactionWinCount;
    }
}
