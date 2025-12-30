package com.psio.trading;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    @Test
    void testTryToSellAssetsWhileNotHavingAnyTest(){
        Wallet wallet = new Wallet(0,0,"Test");
        float assets = wallet.getAssetAmount();

        wallet.trySellAllAssets();

        assertEquals(assets, wallet.getAssetAmount());
    }

    @Test
    void testTryToBuyAssetsWhileBalanceEqualsZeroTest(){
        Wallet wallet = new Wallet(0,0,"Test");
        float assets = wallet.getAssetAmount();

        wallet.tryBuyMaxAssets();

        assertEquals(assets, wallet.getAssetAmount());
    }
}