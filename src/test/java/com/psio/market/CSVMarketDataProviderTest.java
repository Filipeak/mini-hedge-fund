package com.psio.market;

import java.io.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVMarketDataProviderTest {

    private MarketDataNotifier notifier = new MarketDataNotifier();


    @Test
    void allPayloadsCreatedTest() throws ValueBelowZeroException {
        String CSV_DATA_FILE = "src/main/resources/data.csv";
        CSVMarketDataProvider provider = new CSVMarketDataProvider(CSV_DATA_FILE);
        Observer observer = new Observer();
        int payloadsInCSVFile = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_DATA_FILE))) {
            // reading line with description
            reader.readLine();

            while (reader.readLine() != null)
                payloadsInCSVFile++;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        notifier.addObserver(observer);
        provider.getData(notifier);
        assertEquals(payloadsInCSVFile, observer.getCounter());
    }
}

