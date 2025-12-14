package com.psio;

import com.psio.market.CSVMarketDataProvider;
import com.psio.market.MarketDataNotifier;
import com.psio.trading.*;

public class Main {
    public static void main(String[] args) {

        CSVMarketDataProvider csvMarketDataProvider = new CSVMarketDataProvider("src/main/resources/data.csv");
        MarketDataNotifier marketDataNotifier = new MarketDataNotifier();


        TradingAgent tradingAgent1 = new ConservativeTradingAgent(new Wallet(10000.0f, 0.0f));
        marketDataNotifier.addObserver(tradingAgent1);

        TradingAgent tradingAgent2 = new SmartTradingAgent(new Wallet(10000.0f, 0.0f));
        marketDataNotifier.addObserver(tradingAgent2);


        csvMarketDataProvider.getData(marketDataNotifier);


        System.out.println();
        tradingAgent1.getWalletInfo();
        System.out.println();
        tradingAgent2.getWalletInfo();

    }
}
