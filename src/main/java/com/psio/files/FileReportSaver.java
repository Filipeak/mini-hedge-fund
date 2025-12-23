package com.psio.files;

import com.psio.reporting.ReportMetrics;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileReportSaver implements ReportSaver {
    private String filePath;

    public FileReportSaver(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveReport(ReportMetrics metrics) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("=== RAPORT SYMULACJI ===");
            writer.newLine();
            writer.write("Data generowania: " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            writer.newLine();
            writer.write("------------------------");
            writer.newLine();
            writer.write(String.format("Finalny Balans:   %.2f PLN", metrics.finalBalance()));
            writer.write(String.format("Zwrot (ROR):      %.2f %%", metrics.ror()));
            writer.newLine();
            writer.write(String.format("Max Drawdown:     %.2f %%", metrics.maxDrawdown()));
            writer.write(String.format("Win Rate:         %.2f %%", metrics.winRate()));
            writer.newLine();
            writer.write("========================");

            System.out.println("Raport zapisany w: " + filePath);
        } catch (IOException e) {
            System.err.println("Błąd zapisu raportu: " + e.getMessage());
        }
    }
}