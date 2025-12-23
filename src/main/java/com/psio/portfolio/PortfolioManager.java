package com.psio.portfolio;

import com.psio.market.MarketDataObserver;
import com.psio.market.MarketDataPayload;
import com.psio.trading.agents.TradingAgent;

import java.util.ArrayList;
import java.util.List;

public class PortfolioManager implements MarketDataObserver{
    private final TradingAgent[] tradingAgents;
    private final List<PortfolioObserver> observers;

    public PortfolioManager(TradingAgent[] tradingAgents) {
        this.tradingAgents = tradingAgents;
        observers = new ArrayList<>();
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        for (TradingAgent tradingAgent : tradingAgents) {
            tradingAgent.update(marketDataPayload);
        }

        for (PortfolioObserver observer : observers) {
            observer.onChange();
        }
    }

    @Override
    public void begin(){
        for (TradingAgent tradingAgent : tradingAgents) {
            tradingAgent.begin();
        }

        for (PortfolioObserver observer : observers) {
            observer.onBegin();
        }
    }

    @Override
    public void end(){
        for (TradingAgent tradingAgent : tradingAgents) {
            tradingAgent.end();
        }

        for (PortfolioObserver observer : observers) {
            observer.onEnd();
        }
    }

    public void addObserver(PortfolioObserver portfolioObserver){
        if (!observers.contains(portfolioObserver)){
            observers.add(portfolioObserver);
        }
    }

    public void removeObserver(PortfolioObserver portfolioObserver){
        observers.remove(portfolioObserver);
    }

    public float getCurrentValue(){
        float result = 0;

        for(TradingAgent tradingAgent : tradingAgents){
            result += tradingAgent.getWallet().getTotalValue();
        }

        return result;
    }

    public TradingAgent[] getAgents(){
        return tradingAgents;
    }
}
