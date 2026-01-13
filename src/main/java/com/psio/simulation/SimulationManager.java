package com.psio.simulation;

import com.psio.market.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class SimulationManager {

    private static final Logger logger = LogManager.getLogger(SimulationManager.class);

    private final MarketDataNotifier marketDataNotifier;

    public SimulationManager(MarketDataNotifier marketDataNotifier) {
        this.marketDataNotifier = marketDataNotifier;
    }

    public void loadAndRunSimulation(File file) {
        try {
            MarketDataProvider provider = createProvider(file);
            provider.getData(marketDataNotifier);
        } catch (ValueBelowZeroException | IOException e) {
            logger.error("Error loading MarketDataProvider: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

     MarketDataProvider createProvider(File file) throws IOException {
        String path = file.getAbsolutePath();
        if (file.getName().toLowerCase().endsWith(".json")) {
            return new JSONMarketDataProvider(path);
        } else if (file.getName().toLowerCase().endsWith(".csv")) {
            return new CSVMarketDataProvider(path);
        } else {
            throw new IOException("Unsupported file type: " + file.getName());
        }
    }
}