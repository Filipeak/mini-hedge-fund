package com.psio.ui;

import com.psio.market.MarketDataNotifier;
import com.psio.trading.TradingAgent;

import java.util.List;

public class SimulationContext {
    private static List<TradingAgent> agents;
    private static MarketDataNotifier notifier;

    public static void setSimulationData(List<TradingAgent> agentsList, MarketDataNotifier marketNotifier) {
        agents = agentsList;
        notifier = marketNotifier;
    }

    public static List<TradingAgent> getAgents() {
        return agents;
    }

    public static MarketDataNotifier getNotifier() {
        return notifier;
    }
}