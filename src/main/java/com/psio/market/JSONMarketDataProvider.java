package com.psio.market;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JSONMarketDataProvider implements MarketDataProvider {
    private String filePath;

    public JSONMarketDataProvider(String filePath) {
        this.filePath = filePath;
    }


    public void getData(MarketDataNotifier marketDataNotifier) {
        try {
            BufferedReader breader = new BufferedReader(new FileReader(filePath));
            breader.readLine();
            breader.readLine();

            while (breader.ready()) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < 6; i++) {
                    builder.append(breader.readLine());
                }

                String[] data = builder.toString().split("[:,]");

                MarketDataPayload marketDataPayload = new MarketDataPayload(
                        Long.parseLong(data[1].trim()),
                        Float.parseFloat(data[3]),
                        Float.parseFloat(data[5]),
                        Float.parseFloat(data[7]),
                        Float.parseFloat(data[9]),
                        Double.parseDouble(data[11])
                );

                breader.readLine();
                breader.readLine();

                marketDataNotifier.notifyObservers(marketDataPayload);
                //  System.out.println(marketDataPayload);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
