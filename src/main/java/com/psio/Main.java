package com.psio;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.JSONMarketDataProvider;
import com.psio.market.MarketDataNotifier;

public class Main {
    public static void main(String[] args) {

        CSVMarketDataProvider csvMarketDataProvider = new CSVMarketDataProvider("src/main/resources/data.csv");
        JSONMarketDataProvider jsonMarketDataProvider = new JSONMarketDataProvider("src/main/resources/data.json");
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();
        jsonMarketDataProvider.getData(marketDataNotifier);
        csvMarketDataProvider.getData(marketDataNotifier);
    }
}