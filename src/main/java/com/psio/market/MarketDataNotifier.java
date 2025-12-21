package com.psio.market;

import com.psio.trading.PortfolioObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MarketDataNotifier {
    private final HashSet<MarketDataObserver> observers = new HashSet<MarketDataObserver>();
    //TODO Move to PortfolioManager when PortfolioManager will be created
    private final List<PortfolioObserver> portfolioObservers = new ArrayList<>();

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
        if (observers != null) {
            for (MarketDataObserver observer : observers) {
                observer.update(marketDataPayload);
            }
        }
        //TODO Move to PortfolioManager when PortfolioManager will be created
        for (PortfolioObserver observer : portfolioObservers) {
            observer.onChange(marketDataPayload);
        }
    }
}
