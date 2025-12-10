package com.psio;

import com.psio.market.CSVMarketDataProvider;

public class Main {
    public static void main(String[] args) {

        CSVMarketDataProvider mdp = new CSVMarketDataProvider("src/main/resources/data.csv");
        mdp.getData();
    }
}