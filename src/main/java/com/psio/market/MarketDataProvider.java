package com.psio.market;

import java.util.ArrayList;
import java.util.List;

public abstract class MarketDataProvider {
    private List<MarketDataObserver> observers = new ArrayList<>();

    public abstract void getData();

    public void addObserver(MarketDataObserver marketDataObserver) {
        observers.add(marketDataObserver);
    }

    public void removeObserver(MarketDataObserver marketDataObserver) {
        observers.remove(marketDataObserver);
    }

    protected void notifyObserver(MarketDataPayload marketDataPayload) {
        for (MarketDataObserver observer : observers) {
            observer.update(marketDataPayload);
        }
    }
}
