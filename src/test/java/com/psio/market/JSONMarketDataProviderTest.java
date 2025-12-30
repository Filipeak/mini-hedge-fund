package com.psio.market;

import java.io.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JSONMarketDataProviderTest {

    private MarketDataNotifier notifier = new MarketDataNotifier();

    @Test
    void testAllPayloadsCreatedTest() throws ValueBelowZeroException {
        JSONMarketDataProvider provider = new JSONMarketDataProvider("src/main/resources/data.json");
        DummyObserver observer = new DummyObserver();

        notifier.addObserver(observer);
        provider.getData(notifier);

        assertEquals(24 * 365, observer.getCounter());
    }
}
