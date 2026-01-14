package com.psio.simulation;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.JSONMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SimulationManagerTest {
    private final MarketDataNotifier marketDataNotifier = new MarketDataNotifier();
    private final SimulationManager simulationManager = new SimulationManager(marketDataNotifier);

    @Test
    void testThrewExceptionWhileIncorrectFile() {
        File file = new File("File.txt");

        assertThrows(IOException.class, () -> simulationManager.createProvider(file));
    }

    @Test
    void testLoadAndRunSimulationWithCSVFile() throws IOException {
        File file = new File("src/main/resources/data.csv");

        assertInstanceOf(CSVMarketDataProvider.class, simulationManager.createProvider(file));
    }


    @Test
    void testLoadAndRunSimulationWithJSONFile() throws IOException {
        File file = new File("src/main/resources/data.json");

        assertInstanceOf(JSONMarketDataProvider.class, simulationManager.createProvider(file));
    }
}