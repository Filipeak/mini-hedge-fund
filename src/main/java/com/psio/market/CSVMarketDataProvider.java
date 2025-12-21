package com.psio.market;

import java.io.*;
import java.time.*;

public class CSVMarketDataProvider implements MarketDataProvider {
    private final String filePath;

    public CSVMarketDataProvider(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void getData(MarketDataNotifier marketDataNotifier) {
        File file = new File(filePath);

        try (BufferedReader breader = new BufferedReader(new FileReader(file))) {
            breader.readLine();
            String line;

            while ((line = breader.readLine()) != null) {
                String[] data = line.split(",");

                MarketDataPayload marketDataPayload = new MarketDataPayload(
                        Long.parseLong(data[0]),
                        Float.parseFloat(data[1]),
                        Float.parseFloat(data[2]),
                        Float.parseFloat(data[3]),
                        Float.parseFloat(data[4]),
                        Double.parseDouble(data[5])
                );

                marketDataNotifier.notifyObservers(marketDataPayload);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
