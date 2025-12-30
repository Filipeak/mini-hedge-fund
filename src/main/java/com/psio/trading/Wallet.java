package com.psio.trading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger logger = LogManager.getLogger(Wallet.class);

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
        this.transactionBuyPrice = 0;
    }

    public void printInfo() {
        System.out.println();
        logger.info(
                "{}\nBalance: {}\nAsset amount: {}\nTransaction count: {}\nTransactions won: {}",
                name, balance, assetAmount, transactionCount, transactionWinCount
        );
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
            logger.debug(
                    "[{} LOG]: Purchase of {} for {}",
                    name, balance / currentPrice, currentPrice
            );

            this.assetAmount = balance / currentPrice;
            this.balance = 0;
            this.transactionBuyPrice = currentPrice;
        }
    }

    public void trySellAllAssets() {
        if (assetAmount > 0) {
            logger.debug(
                    "[{} LOG]: Sell of {} for {}",
                    name, assetAmount, currentPrice
            );

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
