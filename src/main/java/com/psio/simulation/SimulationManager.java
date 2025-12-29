package com.psio.simulation;

import com.psio.market.*;

import java.io.File;

public class SimulationManager {

    private final MarketDataNotifier marketDataNotifier;

    public SimulationManager(MarketDataNotifier marketDataNotifier) {
        this.marketDataNotifier = marketDataNotifier;
    }

    public void loadAndRunSimulation(File file) {
        Thread simulationThread = new Thread(() -> {
            doLoadAndRunSimulation(file);
        });

        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    public void doLoadAndRunSimulation(File file) {
        try {
            MarketDataProvider provider = createProvider(file);
            provider.getData(marketDataNotifier);
        } catch (ValueBelowZeroException e) {
            throw new RuntimeException(e);
        }
    }

    private MarketDataProvider createProvider(File file) {
        String path = file.getAbsolutePath();
        if (file.getName().toLowerCase().endsWith(".json")) {
            return new JSONMarketDataProvider(path);
        } else {
            return new CSVMarketDataProvider(path);
        }
    }
}