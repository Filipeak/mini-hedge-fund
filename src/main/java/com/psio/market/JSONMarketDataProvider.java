package com.psio.market;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JSONMarketDataProvider implements MarketDataProvider {
    private final String filePath;

    public JSONMarketDataProvider(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void getData(MarketDataNotifier marketDataNotifier) {
        try {
            BufferedReader breader = new BufferedReader(new FileReader(filePath));
            boolean valueBelowZero;

            breader.readLine();
            breader.readLine();

            marketDataNotifier.beginObservers();

            while (breader.ready()) {
                valueBelowZero = false;
                try {
                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; i < 6; i++) {
                        builder.append(breader.readLine());
                    }

                    String[] data = builder.toString().split("[:,]");

                    for (int i = 1; i <= data.length; i= i+2) {
                        if ((Double.parseDouble(data[i]) <= 0)) {
                            valueBelowZero = true;
                            break;
                        }
                    }

                    if (valueBelowZero) {
                        throw new ValueBelowZeroException("Incorrect value");
                    }


                    MarketDataPayload marketDataPayload = new MarketDataPayload(
                            Long.parseLong(data[1].trim()),
                            Float.parseFloat(data[3]),
                            Float.parseFloat(data[5]),
                            Float.parseFloat(data[7]),
                            Float.parseFloat(data[9]),
                            Double.parseDouble(data[11])
                    );



                    marketDataNotifier.updateObservers(marketDataPayload);
                }catch (ValueBelowZeroException e){
                    System.out.println("LOG: Couldn't get payload: " + e);
                } finally {
                    breader.readLine();
                    breader.readLine();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            marketDataNotifier.endObservers();
        }
    }
}
