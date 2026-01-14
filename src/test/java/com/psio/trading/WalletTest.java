package com.psio.trading;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    @Test
    void testTryToSellAssetsWhileNotHavingAnyTest() {
        Wallet wallet = new Wallet(0, 0, "Test");
        float assets = wallet.getAssetAmount();

        wallet.trySellAllAssets();

        assertEquals(assets, wallet.getAssetAmount());
    }

    @Test
    void testTryToBuyAssetsWhileBalanceEqualsZeroTest() {
        Wallet wallet = new Wallet(0, 0, "Test");
        float assets = wallet.getAssetAmount();

        wallet.tryBuyMaxAssets();

        assertEquals(assets, wallet.getAssetAmount());
    }

    @Test
    void testIsResetProperWorkingInWallet() {
        Wallet defaultWallet = new Wallet(100, 0, "Test");
        Wallet wallet = new Wallet(100, 0, "Test");

        wallet.updateCurrentPrice(5);
        wallet.tryBuyMaxAssets();
        wallet.reset();

        assertEquals(defaultWallet.getBalance(), wallet.getBalance());
        assertEquals(defaultWallet.getAssetAmount(), wallet.getAssetAmount());
    }
}