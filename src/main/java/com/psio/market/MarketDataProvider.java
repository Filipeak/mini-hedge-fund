package com.psio.market;


public interface MarketDataProvider {

    public void getData(MarketDataNotifier marketDataNotifier) throws ValueBelowZeroException;
}
