package com.psio.trading;

public class Wallet {
    private float balance;
    private float assetAmount;

    public Wallet(float balance, float assetAmount) {
        this.balance = balance;
        this.assetAmount = assetAmount;
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

    public void buyAssets(float currentPrice) {
        if (balance > 0) {
            System.out.println("!!! Purchase of " + balance / currentPrice + " for " + currentPrice);

            this.assetAmount = balance / currentPrice;
            this.balance = 0;
        } //else throw new InvalidTransactionException("Insufficient balance");

    }

    public void sellAssets(float currentPrice) {
        if (assetAmount > 0) {
            System.out.println("!!! Sell of " + assetAmount + " for " + currentPrice);

            this.balance = balance + assetAmount * currentPrice;
            this.assetAmount = 0;
        } //else throw new InvalidTransactionException("Insufficient assets");
    }

}
