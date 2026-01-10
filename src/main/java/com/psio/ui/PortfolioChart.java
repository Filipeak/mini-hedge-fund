package com.psio.ui;

import com.psio.portfolio.PortfolioManager;
import com.psio.portfolio.PortfolioObserver;
import com.psio.portfolio.ValueProvider;
import com.psio.trading.agents.TradingAgent;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class PortfolioChart implements PortfolioObserver {

    private static final Logger logger = LogManager.getLogger(PortfolioChart.class);

    private XYChart.Series<Number, Number> series;
    private final PortfolioManager portfolioManager;

    private final Map<String, List<Snapshot>> histories = new HashMap<>();

    private final Map<String, Float> initialCapitals = new LinkedHashMap<>();

    private String currentDataSourceName;

    private boolean percentageMode = false;
    private NumberAxis yAxis;

    private record Snapshot(long timestamp, float currentTotalValue) {}

    public PortfolioChart(PortfolioManager portfolioManager) {
        this.portfolioManager = portfolioManager;

        this.currentDataSourceName = portfolioManager.getName();
        prepareDataSource(portfolioManager);

        for (TradingAgent agent : portfolioManager.getAgents()) {
            prepareDataSource(agent.getWallet());
        }

        portfolioManager.addObserver(this);
    }

    private void prepareDataSource(ValueProvider provider) {
        histories.put(provider.getName(), new ArrayList<>());
        initialCapitals.put(provider.getName(), provider.getCurrentValue());
        logger.info("Registered data source: {} with initial capital: {}", provider.getName(), provider.getCurrentValue());
    }

    public Set<String> getAvailableDataSources() {
        return initialCapitals.keySet();
    }

    public String getDefaultDataSourceName() {
        return portfolioManager.getName();
    }

    public void setDataSource(String name) {
        if (histories.containsKey(name)) {
            this.currentDataSourceName = name;
            Platform.runLater(this::refreshSeries);
        }
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

        List<Snapshot> currentHistory = histories.get(currentDataSourceName);
        Float initialCap = initialCapitals.get(currentDataSourceName);

        if (currentHistory != null && initialCap != null) {
            List<Snapshot> snapshotCopy;
            synchronized (histories) {
                snapshotCopy = new ArrayList<>(currentHistory);
            }

            for (Snapshot s : snapshotCopy) {
                addPointToSeries(s.timestamp, s.currentTotalValue, initialCap);
            }
        }
    }

    private void addPointToSeries(long timestamp, float currentTotalValue, float initialCap) {
        float valueToPlot;
        float profitOrLoss = currentTotalValue - initialCap;

        if (percentageMode) {
            if (initialCap != 0) {
                valueToPlot = (profitOrLoss / initialCap) * 100;
            } else {
                valueToPlot = 0;
            }
        } else {
            valueToPlot = profitOrLoss;
        }

        series.getData().add(new XYChart.Data<>(timestamp, valueToPlot));
    }

    @Override
    public void onChange() {
        long timestamp = portfolioManager.getLastTimestamp();

        synchronized (histories) {
            histories.get(portfolioManager.getName())
                    .add(new Snapshot(timestamp, portfolioManager.getCurrentValue()));

            for (TradingAgent agent : portfolioManager.getAgents()) {
                String name = agent.getWallet().getName();
                float value = agent.getWallet().getCurrentValue();

                if (histories.containsKey(name)) {
                    histories.get(name).add(new Snapshot(timestamp, value));
                }
            }
        }
    }

    @Override
    public void onBegin() {
        synchronized (histories) {
            for (List<Snapshot> list : histories.values()) {
                list.clear();
            }
        }
    }

    @Override
    public void onEnd() {
        Platform.runLater(this::refreshSeries);
    }
}