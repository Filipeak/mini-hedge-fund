package com.psio.market;

import java.io.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONMarketDataProviderTest {

    private MarketDataNotifier notifier = new MarketDataNotifier();


    @Test
    void allPayloadsCreatedTest() throws ValueBelowZeroException {
        String JSON_DATA_FILE = "src/main/resources/data.json";
        JSONMarketDataProvider provider = new JSONMarketDataProvider(JSON_DATA_FILE);
        Observer observer = new Observer();
        int payloadsInJSONFile = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(JSON_DATA_FILE))) {

            reader.readLine();
            reader.readLine();

            while (reader.ready()) {
                //i < 8 because, 8 is the number of lines that include values in JSON and }, {
                for (int i = 0; i < 8; i++) {
                    reader.readLine();
                }
                payloadsInJSONFile++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        notifier.addObserver(observer);
        provider.getData(notifier);
        assertEquals(payloadsInJSONFile, observer.getCounter());
    }
}