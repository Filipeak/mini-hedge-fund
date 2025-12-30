package com.psio.trading;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

   private Wallet wallet = new Wallet(0,0,"Test");

    @Test
    void TryToSellAssetsWhileNotHavingAnyTest(){
        float assets = wallet.getAssetAmount();
        wallet.trySellAllAssets();
        assertEquals(assets, wallet.getAssetAmount());
    }

    @Test
    void TryToBuyAssetsWhileBalanceEqualsZeroTest(){
        float assets = wallet.getAssetAmount();
        wallet.tryBuyMaxAssets();
        assertEquals(assets, wallet.getAssetAmount());
    }
}