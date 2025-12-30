package com.psio.reporting;

import com.psio.portfolio.PortfolioManager;
import com.psio.portfolio.PortfolioObserver;
import com.psio.reporting.creators.WriterCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class ReportCreator implements PortfolioObserver {

    private static final Logger logger = LogManager.getLogger(ReportCreator.class);

    private final WriterCreator writerCreator;
    private final PortfolioManager portfolioManager;

    private float initialTotalBalance = 0;
    private float maxPeakValue = 0;
    private float maxDrawdown = 0;

    public ReportCreator(PortfolioManager portfolioManager, WriterCreator writerCreator) {
        this.portfolioManager = portfolioManager;
        this.writerCreator = writerCreator;
        portfolioManager.addObserver(this);
    }

    @Override
    public void onBegin() {
        initialTotalBalance = portfolioManager.getCurrentValue();
        maxPeakValue = initialTotalBalance;
        maxDrawdown = 0;
    }

    @Override
    public void onChange() {
        float currentTotalValue = portfolioManager.getCurrentValue();

        if (currentTotalValue > maxPeakValue) {
            maxPeakValue = currentTotalValue;
        }

        if (maxPeakValue > 0) {
            float currentDrawdown = (currentTotalValue - maxPeakValue) / maxPeakValue * 100;
            if (currentDrawdown < maxDrawdown) {
                maxDrawdown = currentDrawdown;
            }
        }
    }

    @Override
    public void onEnd() {
        float finalValue = portfolioManager.getCurrentValue();

        double ror = ((finalValue - initialTotalBalance) / initialTotalBalance) * 100;

        long totalTransactions = Arrays.stream(portfolioManager.getAgents())
                .mapToLong(a -> a.getWallet().getTransactionCount())
                .sum();

        long totalWins = Arrays.stream(portfolioManager.getAgents())
                .mapToLong(a -> a.getWallet().getTransactionWinCount())
                .sum();

        double winRate = 0;
        if (totalTransactions > 0) {
            winRate = (double) totalWins / totalTransactions * 100;
        }

        ReportMetrics metrics = new ReportMetrics(ror, maxDrawdown, winRate, finalValue);

        try (Writer writer = writerCreator.createWriter()) {
            writer.write(metrics.toString());
        } catch (IOException e) {
            logger.error("Report error: {}", e.getMessage());
        }
    }
}