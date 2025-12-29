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
            MarketDataProvider provider = createProvider(file);

            try {
                provider.getData(marketDataNotifier);
            } catch (ValueBelowZeroException e) {
                throw new RuntimeException(e);
            }
        });

        simulationThread.setDaemon(true);
        simulationThread.start();
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