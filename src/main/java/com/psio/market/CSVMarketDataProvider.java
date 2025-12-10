package com.psio.market;


import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class CSVMarketDataProvider extends MarketDataProvider {
    private String filePath;

    public CSVMarketDataProvider(String filePath) {
        this.filePath = filePath;
    }

    public void getData() {
        File file = new File(filePath);
        String splitBy = ",";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (BufferedReader breader = new BufferedReader(new FileReader(file))) {
            breader.readLine();
            String line = breader.readLine();
            while ((line != null)) {
                String[] data = line.split(splitBy);
                MarketDataPayload mdp = new MarketDataPayload(
                        (int) LocalDateTime.parse(data[0], formatter).toEpochSecond(ZoneOffset.UTC),
                        Float.parseFloat(data[1]),
                        Float.parseFloat(data[2]),
                        Float.parseFloat(data[3]),
                        Float.parseFloat(data[4]),
                        Double.parseDouble(data[5]));
                this.notifyObserver(mdp);
                line = breader.readLine();
                System.out.println(mdp.timestamp + " " + mdp.open + " " + mdp.close + " " + mdp.high + " " + mdp.low + " " + mdp.volume);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
