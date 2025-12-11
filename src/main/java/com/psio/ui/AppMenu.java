package com.psio.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class AppMenu {

    private final Stage stage;
    private final DataLoader dataLoader;
    private final PortfolioChart portfolioChart;

    public AppMenu(Stage stage, DataLoader dataLoader, PortfolioChart portfolioChart) {
        this.stage = stage;
        this.dataLoader = dataLoader;
        this.portfolioChart = portfolioChart;
    }

    public MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("Plik");
        MenuItem itemOpen = new MenuItem("Wczytaj CSV...");
        itemOpen.setOnAction(this::handleOpenFile);
        menuFile.getItems().add(itemOpen);

        Menu menuHelp = new Menu("Pomoc");
        MenuItem itemAbout = new MenuItem("O programie");
        itemAbout.setOnAction(_ -> showAboutDialog());
        menuHelp.getItems().add(itemAbout);

        menuBar.getMenus().addAll(menuFile, menuHelp);
        return menuBar;
    }

    private void handleOpenFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik z danymi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki CSV", "*.csv"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                List<PortfolioData> data = dataLoader.loadDataFromFile(file);
                portfolioChart.updateData(data);
            } catch (Exception e) {
                showErrorDialog("Plik jest niepoprawny lub uszkodzony.\n" + e.getMessage());
            }
        }
    }

    private void showErrorDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd odczytu pliku");
        alert.setHeaderText("Nie udało się wczytać danych");
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CryptoPortfolioApp.CSS_PATH)).toExternalForm());
        dialogPane.getStyleClass().add("my-dialog");

        alert.showAndWait();
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("O Aplikacji");
        alert.setHeaderText("Portfolio Tracker v1.1");
        alert.setContentText("Bubuś");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CryptoPortfolioApp.CSS_PATH)).toExternalForm());
        dialogPane.getStyleClass().add("my-dialog");

        alert.showAndWait();
    }
}