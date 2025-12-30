package com.psio.market;

import java.util.ArrayList;

public class MarketDataNotifier {
    private final ArrayList<MarketDataObserver> observers = new ArrayList<>();

    public void addObserver(MarketDataObserver marketDataObserver) {
       if(!observers.contains(marketDataObserver))
        observers.add(marketDataObserver);
    }

    public void removeObserver(MarketDataObserver marketDataObserver) {
        observers.remove(marketDataObserver);
    }

    public void updateObservers(MarketDataPayload marketDataPayload) {
        for (MarketDataObserver observer : observers) {
            observer.update(marketDataPayload);
        }
    }

    public void beginObservers() {
        for (MarketDataObserver observer : observers) {
            observer.begin();
        }
    }

    public void endObservers() {
        for (MarketDataObserver observer : observers) {
            observer.end();
        }
    }

    public ArrayList<MarketDataObserver> getObservers() {
        return observers;
    }
}
