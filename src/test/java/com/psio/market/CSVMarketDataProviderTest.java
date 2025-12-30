package com.psio.market;

import java.io.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CSVMarketDataProviderTest {

    private MarketDataNotifier notifier = new MarketDataNotifier();

    @Test
    void testAllPayloadsCreatedTest() throws ValueBelowZeroException {
        CSVMarketDataProvider provider = new CSVMarketDataProvider("src/main/resources/data.csv");
        DummyObserver observer = new DummyObserver();

        notifier.addObserver(observer);
        provider.getData(notifier);

        assertEquals(24 * 365, observer.getCounter());
    }
}
