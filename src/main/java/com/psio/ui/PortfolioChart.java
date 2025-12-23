package com.psio.ui;

import com.psio.market.MarketDataPayload;
import com.psio.trading.PortfolioObserver;
import com.psio.trading.TradingAgent;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PortfolioChart implements PortfolioObserver {

    private XYChart.Series<Number, Number> series;
    private final List<TradingAgent> agents;
    private final List<Snapshot> history = new ArrayList<>();

    private final float fixedInitialCapital;

    private boolean percentageMode = false;
    private NumberAxis yAxis;

    private record Snapshot(long timestamp, float currentTotalValue) {}

    public PortfolioChart(List<TradingAgent> agents) {
        this.agents = agents;
        this.fixedInitialCapital = calculateCurrentTotalBalance();

        System.out.println("PortfolioChart: Base for percentage calculations set at: " + fixedInitialCapital + " PLN");
    }

    public LineChart<Number, Number> createChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Data");
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        xAxis.setTickLabelFormatter(new StringConverter<>() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            public String toString(Number t) { return sdf.format(new Date(t.longValue())); }
            @Override
            public Number fromString(String s) { return 0; }
        });

        yAxis = new NumberAxis();
        yAxis.setLabel("Zysk / Strata (PLN)");
        yAxis.setAutoRanging(true);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Symulacja Live");
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        series = new XYChart.Series<>();
        series.setName("PnL");
        lineChart.getData().add(series);

        return lineChart;
    }

    public void toggleViewMode() {
        percentageMode = !percentageMode;
        yAxis.setLabel(percentageMode ? "Zwrot z inwestycji (%)" : "Zysk / Strata (PLN)");
        Platform.runLater(this::refreshSeries);
    }

    private void refreshSeries() {
        series.getData().clear();
        for (Snapshot s : history) {
            addPointToSeries(s.timestamp, s.currentTotalValue);
        }
    }

    private void addPointToSeries(long timestamp, float currentTotalValue) {
        float valueToPlot;

        float profitOrLoss = currentTotalValue - fixedInitialCapital;

        if (percentageMode) {
            if (fixedInitialCapital != 0) {
                valueToPlot = (profitOrLoss / fixedInitialCapital) * 100;
            } else {
                valueToPlot = 0;
            }
        } else {
            valueToPlot = profitOrLoss;
        }

        series.getData().add(new XYChart.Data<>(timestamp, valueToPlot));
    }

    @Override
    public void onChange(MarketDataPayload marketDataPayload) {
        float currentTotalValue = 0;
        for (TradingAgent agent : agents) {
            currentTotalValue += agent.getBalance() + (agent.getAssets() * marketDataPayload.close);
        }

        long timestamp = marketDataPayload.timestamp;

        synchronized (history) {
            history.add(new Snapshot(timestamp, currentTotalValue));
        }
    }

    @Override
    public void onBegin() {
        history.clear();
    }

    @Override
    public void onEnd() {
        Platform.runLater(this::refreshSeries);
    }

    private float calculateCurrentTotalBalance() {
        float total = 0;
        for (TradingAgent agent : agents) {
            total += agent.getBalance();
        }
        return total;
    }
}