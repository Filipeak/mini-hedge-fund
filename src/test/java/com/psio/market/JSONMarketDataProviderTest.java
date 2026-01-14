package com.psio.market;

import java.io.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JSONMarketDataProviderTest {

    private final DummyObserver observer = new DummyObserver();
    private final MarketDataNotifier notifier = new MarketDataNotifier();

    @Test
    void testAllPayloadsCreatedTest() throws ValueBelowZeroException {
        JSONMarketDataProvider provider = new JSONMarketDataProvider("src/main/resources/data.json");

        notifier.addObserver(observer);
        provider.getData(notifier);

        assertEquals(24 * 365, observer.getCounter());
    }

    @Test
    void testExceptionThrewWhileIncorrectData() {
        JSONMarketDataProvider provider = new JSONMarketDataProvider("src/test/java/com/psio/market/incorrectData.json");

        notifier.addObserver(observer);

        assertThrows(ValueBelowZeroException.class, () -> provider.getData(notifier));
    }

    @Test
    void testExceptionThrewWhileFileDoesntExist() {
        JSONMarketDataProvider provider = new JSONMarketDataProvider("noFile.json");

        notifier.addObserver(observer);

        assertThrows(RuntimeException.class, () -> provider.getData(notifier));
    }
}
