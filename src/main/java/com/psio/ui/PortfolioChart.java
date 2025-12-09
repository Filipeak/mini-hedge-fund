package com.psio.ui;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PortfolioChart {

    private XYChart.Series<Number, Number> series;
    private NumberAxis xAxis;

    private final DecimalFormat currencyFormat;

    public PortfolioChart() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator('.');

        this.currencyFormat = new DecimalFormat("#,##0.00", symbols);
    }

    public LineChart<Number, Number> createChart() {
        xAxis = new NumberAxis();
        xAxis.setLabel("Data");
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wartość portfela (PLN)");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Historia Portfela");
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(false);

        series = new XYChart.Series<>();
        series.setName("Portfel");
        lineChart.getData().add(series);

        return lineChart;
    }

    public void updateData(List<PortfolioData> newData) {
        series.getData().clear();

        int dataSize = newData.size();
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(dataSize > 0 ? dataSize - 1 : 10);

        xAxis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number object) {
                int index = object.intValue();
                if (index >= 0 && index < newData.size()) {
                    return newData.get(index).date().format(DateTimeFormatter.ofPattern("dd MMM"));
                }
                return "";
            }

            public Number fromString(String string) {
                return 0;
            }
        });

        for (int i = 0; i < newData.size(); i++) {
            PortfolioData entry = newData.get(i);
            var dataPoint = new XYChart.Data<Number, Number>(i, entry.value());

            String dateStr = entry.date().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            String formattedValue = currencyFormat.format(entry.value());

            String tooltipText = String.format("Data: %s\nWartość: %s PLN", dateStr, formattedValue);

            dataPoint.nodeProperty().addListener((ObservableValue<? extends Node> _, Node _, Node newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip(tooltipText);
                    tooltip.setShowDelay(Duration.millis(50));
                    Tooltip.install(newNode, tooltip);
                    newNode.setOnMouseEntered(_ -> newNode.setStyle("-fx-scale-x: 1.5; -fx-scale-y: 1.5; -fx-cursor: hand;"));
                    newNode.setOnMouseExited(_ -> newNode.setStyle("-fx-scale-x: 1.0; -fx-scale-y: 1.0;"));
                }
            });

            series.getData().add(dataPoint);
        }
    }
}