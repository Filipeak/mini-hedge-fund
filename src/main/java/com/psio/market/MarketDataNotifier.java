package com.psio.market;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

public class MarketDataNotifier {

    private static final Logger logger = LogManager.getLogger(MarketDataNotifier.class);

    private final HashSet<MarketDataObserver> observers = new HashSet<>();

    public void addObserver(MarketDataObserver marketDataObserver) {
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
        logger.info("Started reading the file");
        for (MarketDataObserver observer : observers) {
            observer.begin();
        }
    }

    public void endObservers() {
        logger.info("Finished reading the file");
        for (MarketDataObserver observer : observers) {
            observer.end();
        }
    }
}
