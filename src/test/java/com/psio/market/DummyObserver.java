package com.psio.market;

public class DummyObserver implements MarketDataObserver {
    private int counter = 0;

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        counter++;
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    public int getCounter() {
        return counter;
    }
}