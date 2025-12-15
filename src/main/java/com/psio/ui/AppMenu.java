package com.psio.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

public class AppMenu {

    private final Stage stage;
    private final Consumer<File> onFileSelected;

    public AppMenu(Stage stage, Consumer<File> onFileSelected) {
        this.stage = stage;
        this.onFileSelected = onFileSelected;
    }

    public MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("Plik");
        MenuItem itemOpen = new MenuItem("Wczytaj CSV...");
        itemOpen.setOnAction(this::handleOpenFile);
        menuFile.getItems().add(itemOpen);

        Menu menuHelp = new Menu("Pomoc");
        MenuItem itemAbout = new MenuItem("O programie");
        itemAbout.setOnAction(e -> showAboutDialog());
        menuHelp.getItems().add(itemAbout);

        menuBar.getMenus().addAll(menuFile, menuHelp);
        return menuBar;
    }

    private void handleOpenFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik z danymi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki CSV", "*.csv"));

        File initialDir = new File("src/main/resources");
        if (initialDir.exists()) {
            fileChooser.setInitialDirectory(initialDir);
        }

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            onFileSelected.accept(file);
        }
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("O Aplikacji");
        alert.setHeaderText("Portfolio Tracker v2.0");
        alert.setContentText("Integracja z modułem MarketData.");

        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CryptoPortfolioApp.CSS_PATH)).toExternalForm());
            dialogPane.getStyleClass().add("my-dialog");
        } catch (Exception ignored) {}

        alert.showAndWait();
    }
}