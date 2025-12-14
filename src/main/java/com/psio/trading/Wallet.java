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

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(float assetAmount) {
        this.assetAmount = assetAmount;
    }
}
