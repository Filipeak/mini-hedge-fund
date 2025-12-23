package com.psio.reporting;

import com.psio.files.ReportSaver;
import com.psio.market.MarketDataPayload;
import com.psio.portfolio.PortfolioObserver;
import com.psio.trading.agents.TradingAgent;

import java.util.List;

public class ReportCreator implements PortfolioObserver {

    private final List<TradingAgent> agents;
    private final ReportSaver reportSaver;

    private float initialTotalBalance = 0;
    private float maxPeakValue = 0;
    private float maxDrawdown = 0;

    public ReportCreator(List<TradingAgent> agents, ReportSaver reportSaver) {
        this.agents = agents;
        this.reportSaver = reportSaver;
    }

    @Override
    public void onBegin() {
        initialTotalBalance = calculateTotalBalance(0);
        maxPeakValue = initialTotalBalance;
        maxDrawdown = 0;
    }

    @Override
    public void onChange(MarketDataPayload marketDataPayload) {
        float currentTotalValue = calculateTotalBalance(marketDataPayload.close);

        if (currentTotalValue > maxPeakValue) {
            maxPeakValue = currentTotalValue;
        }

        if (maxPeakValue > 0) {
            float currentDrawdown = (maxPeakValue - currentTotalValue) / maxPeakValue * 100;
            if (currentDrawdown > maxDrawdown) {
                maxDrawdown = currentDrawdown;
            }
        }
    }

    @Override
    public void onEnd() {
        float finalValue = calculateTotalBalance(0);

        double ror = ((finalValue - initialTotalBalance) / initialTotalBalance) * 100;

        long winners = agents.stream()
                .filter(a -> a.getBalance() > (initialTotalBalance / agents.size()))
                .count();
        double winRate = (double) winners / agents.size() * 100;

        ReportMetrics metrics = new ReportMetrics(ror, maxDrawdown, winRate, finalValue);

        reportSaver.saveReport(metrics);
    }

    private float calculateTotalBalance(float currentPrice) {
        float total = 0;
        for (TradingAgent agent : agents) {
            total += agent.getBalance();
        }
        return total;
    }
}