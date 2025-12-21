package com.psio.market;

import com.psio.trading.PortfolioObserver;

import java.util.HashSet;
import java.util.Set;

public class MarketDataNotifier {
    private final Set<MarketDataObserver> observers = new HashSet<>();
    //TODO Move to PortfolioManager when PortfolioManager will be created
    private final Set<PortfolioObserver> portfolioObservers = new HashSet<>();

    public void addObserver(MarketDataObserver marketDataObserver) {
        observers.add(marketDataObserver);
    }

    public void addPortfolioObserver(PortfolioObserver portfolioObserver) {
        portfolioObservers.add(portfolioObserver);
    }

    public void removeObserver(MarketDataObserver marketDataObserver) {
        observers.remove(marketDataObserver);
    }

    public void notifyObservers(MarketDataPayload marketDataPayload) {
        for (MarketDataObserver observer : observers) {
            observer.update(marketDataPayload);
        }
        //TODO Move to PortfolioManager when PortfolioManager will be created
        for (PortfolioObserver observer : portfolioObservers) {
            observer.onChange(marketDataPayload);
        }
    }
}
