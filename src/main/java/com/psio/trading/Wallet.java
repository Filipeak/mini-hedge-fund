package com.psio.trading;

import com.psio.portfolio.ValueProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Wallet implements ValueProvider {
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

    public float getBalance() {
        return balance;
    }

    public float getAssetAmount() {
        return assetAmount;
    }

    @Override
    public float getCurrentValue() {
        return balance + assetAmount * currentPrice;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public int getTransactionWinCount() {
        return transactionWinCount;
    }

    public void updateCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void tryBuyMaxAssets() {
        if (balance > 0) {
            logger.debug(
                    "[{}]: Purchase of {} for {}",
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
                    "[{}]: Sell of {} for {}",
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
                "{}\n\tBalance: {}\n\tAsset amount: {}\n\tTransaction count: {}\n\tTransactions won: {}",
                name, balance, assetAmount, transactionCount, transactionWinCount
        );
    }
}
