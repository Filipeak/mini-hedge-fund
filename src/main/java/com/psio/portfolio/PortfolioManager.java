package com.psio.portfolio;

import com.psio.market.MarketDataObserver;
import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAgent;

import java.util.ArrayList;

public class PortfolioManager implements MarketDataObserver{
    TradingAgent[] tradingAgents;
    ArrayList<PortfolioObserver> observers;

    public PortfolioManager(TradingAgent[] tradingAgents) {
        this.tradingAgents = tradingAgents;
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        for (TradingAgent tradingAgent : tradingAgents) {
            tradingAgent.begin();
            tradingAgent.update(marketDataPayload);
            tradingAgent.end();
        }

    }

    @Override
    public void begin(){

    }

    @Override
    public void end(){

    }

    public void addObserver(PortfolioObserver portfolioObserver){
        if (!observers.contains(portfolioObserver)){
            observers.add(portfolioObserver);
        }
    }

    public void removeObserver(PortfolioObserver portfolioObserver){
        observers.remove(portfolioObserver);
    }

    public float getCurrentValue(float currentPrice){
        float result = 0;

        for(TradingAgent tradingAgent : tradingAgents){
            result += tradingAgent.getWallet().getCurrentValue(currentPrice);
        }

        return result;
    }

    public TradingAgent[] getAgents(){
        return tradingAgents;
    }

}
