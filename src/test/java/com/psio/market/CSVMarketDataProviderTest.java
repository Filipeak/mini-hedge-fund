package com.psio.market;

import java.io.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CSVMarketDataProviderTest {
    private final DummyObserver observer = new DummyObserver();
    private final MarketDataNotifier notifier = new MarketDataNotifier();

    @Test
    void testAllPayloadsCreatedTest() throws ValueBelowZeroException {
        CSVMarketDataProvider provider = new CSVMarketDataProvider("src/main/resources/data.csv");

        notifier.addObserver(observer);
        provider.getData(notifier);

        assertEquals(24 * 365, observer.getCounter());
    }

    @Test
    void testExceptionThrewWhileIncorrectData() {
        CSVMarketDataProvider provider = new CSVMarketDataProvider("src/test/java/com/psio/market/incorrectData.csv");

        notifier.addObserver(observer);
        assertThrows(ValueBelowZeroException.class, () -> provider.getData(notifier));
    }

    @Test
    void testExceptionThrewWhileFileDoesntExist() {
        CSVMarketDataProvider provider = new CSVMarketDataProvider("noFile.csv");

        notifier.addObserver(observer);
        assertThrows(RuntimeException.class, () -> provider.getData(notifier));
    }
}
