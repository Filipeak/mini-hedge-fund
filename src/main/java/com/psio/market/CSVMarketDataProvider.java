//package com.psio.market;
//
//
//import java.io.*;
//
//
//public class CSVMarketDataProvider extends MarketDataProvider {
//    public void getData() {
//        File file = new File("src/main/java/com/psio/market/cdr_d.csv");
//        String splitBy = ",";
//        try (BufferedReader breader = new BufferedReader(new FileReader(file))) {
//            breader.readLine();
//            String line = breader.readLine();
//            if ((line != null)) {
//                String[] data = line.split(splitBy);
//                System.out.println(line);
//                MarketDataPayload mdp = new MarketDataPayload(
//                        data[0],
//                        Float.parseFloat(data[1]),
//                        Float.parseFloat(data[4]),
//                        Float.parseFloat(data[2]),
//                        Float.parseFloat(data[3]),
//                        Integer.parseInt(data[5]));
//                System.out.println(mdp.timestamp + " " + mdp.open + " " + mdp.close + " " + mdp.high + " " + mdp.low + " " + mdp.volume);
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//}
