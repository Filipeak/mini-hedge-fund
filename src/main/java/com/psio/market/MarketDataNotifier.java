package com.psio.market;

import java.util.HashSet;

public class MarketDataNotifier {
    private final HashSet<MarketDataObserver> observers = new HashSet<MarketDataObserver>();

    public void addObserver(MarketDataObserver marketDataObserver) {
        observers.add(marketDataObserver);
    }

    public void removeObserver(MarketDataObserver marketDataObserver) {
        observers.remove(marketDataObserver);
    }

    public void notifyObservers(MarketDataPayload marketDataPayload) {
        for (MarketDataObserver observer : observers) {
            observer.update(marketDataPayload);
        }
    }
}
