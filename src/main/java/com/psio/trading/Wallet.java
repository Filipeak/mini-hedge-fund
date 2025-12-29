package com.psio.trading;

public class Wallet {
    private final float defaultBalance;
    private final float defaultAssetAmount;
    private final String name;

    private float balance;
    private float assetAmount;
    private float currentPrice;
    private int transactionCount;
    private int transactionWinCount;
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
        this.currentPrice = 0;
        this.transactionCount = 0;
        this.transactionWinCount = 0;
    }

    public void printInfo() {
        System.out.println("\nWallet " + name
                + "\nBalance: " + balance
                + "\nAsset amount: " + assetAmount
                + "\nTransaction count: " + transactionCount
                + "\nTransactions won: " + transactionWinCount);
    }

    public void updateCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getBalance() {
        return balance;
    }

    public float getAssetAmount() {
        return assetAmount;
    }

    public float getTotalValue() {
        return balance + assetAmount * currentPrice;
    }

    public void tryBuyMaxAssets() {
        if (balance > 0) {
            System.out.println("[" + name + " LOG]: Purchase of " + balance / currentPrice + " for " + currentPrice);

            this.assetAmount = balance / currentPrice;
            this.balance = 0;
            this.transactionBuyPrice = currentPrice;
        }
    }

    public void trySellAllAssets() {
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
