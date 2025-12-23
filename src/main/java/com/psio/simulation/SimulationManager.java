package com.psio.simulation;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.JSONMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import com.psio.market.MarketDataProvider;

import java.io.File;

public class SimulationManager {

    private final MarketDataNotifier marketDataNotifier;

    public SimulationManager(MarketDataNotifier marketDataNotifier) {
        this.marketDataNotifier = marketDataNotifier;
    }

    public void loadAndRunSimulation(File file) {
        Thread simulationThread = new Thread(() -> {
            MarketDataProvider provider = createProvider(file);

            provider.getData(marketDataNotifier);
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