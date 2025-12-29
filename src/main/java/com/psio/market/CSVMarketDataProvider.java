package com.psio.market;

import java.io.*;
import java.time.*;

public class CSVMarketDataProvider implements MarketDataProvider {
    private final String filePath;

    public CSVMarketDataProvider(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void getData(MarketDataNotifier marketDataNotifier) throws ValueBelowZeroException {
        File file = new File(filePath);

        try (BufferedReader breader = new BufferedReader(new FileReader(file))) {
            breader.readLine();
            String line;

            marketDataNotifier.beginObservers();

            while ((line = breader.readLine()) != null) {
                String[] data = line.split(",");

                for (int i = 0; i < data.length; i++) {
                    if ((Double.parseDouble(data[i]) <= 0)) {
                        throw new ValueBelowZeroException("Incorrect value");
                    }
                }

                MarketDataPayload marketDataPayload = new MarketDataPayload(
                        Long.parseLong(data[0]),
                        Float.parseFloat(data[1]),
                        Float.parseFloat(data[2]),
                        Float.parseFloat(data[3]),
                        Float.parseFloat(data[4]),
                        Double.parseDouble(data[5])
                );

                marketDataNotifier.updateObservers(marketDataPayload);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            marketDataNotifier.endObservers();
        }
    }
}
