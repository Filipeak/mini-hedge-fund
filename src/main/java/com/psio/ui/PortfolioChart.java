package com.psio.ui;

import com.psio.market.MarketDataObserver;
import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAgent;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class PortfolioChart implements MarketDataObserver {

    private XYChart.Series<Number, Number> series;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private final List<TradingAgent> agents;

    private double initialTotalValue = 0;
    private int tickCounter = 0;

    public PortfolioChart(List<TradingAgent> agents) {
        this.agents = agents;

        for (TradingAgent agent : agents) {
            initialTotalValue += agent.getBalance();
        }
    }

    public LineChart<Number, Number> createChart() {
        xAxis = new NumberAxis();
        xAxis.setLabel("Czas");
        xAxis.setAutoRanging(true);

        yAxis = new NumberAxis();
        yAxis.setLabel("Zysk / Strata (PLN)");
        yAxis.setAutoRanging(true);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Symulacja Live: Wynik Finansowy");

        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        series = new XYChart.Series<>();
        series.setName("PnL");
        lineChart.getData().add(series);

        return lineChart;
    }

    public void update(MarketDataPayload marketDataPayload) {
        double currentPrice = marketDataPayload.close;
        double currentTotalValue = 0;

        for (TradingAgent agent : agents) {
            currentTotalValue += agent.getBalance() + (agent.getAssets() * currentPrice);
        }

        final double absolutePnL = currentTotalValue - initialTotalValue;
        final int currentTick = tickCounter++;

        Platform.runLater(() -> {
            var dataPoint = new XYChart.Data<Number, Number>(currentTick, absolutePnL);
            series.getData().add(dataPoint);
        });
    }

    public void clear() {
        Platform.runLater(() -> {
            series.getData().clear();
            tickCounter = 0;
        });
    }
}