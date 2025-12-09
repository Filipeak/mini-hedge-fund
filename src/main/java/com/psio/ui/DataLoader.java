package com.psio.ui;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public List<PortfolioData> loadDataFromResource(String resourceName) {
        InputStream inputStream = getClass().getResourceAsStream(resourceName);
        if (inputStream == null) {
            System.err.println("Nie znaleziono zasobu: " + resourceName);
            return new ArrayList<>();
        }
        return parseStream(inputStream);
    }

    public List<PortfolioData> loadDataFromFile(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            List<PortfolioData> data = parseStream(inputStream);

            if (data.isEmpty()) {
                throw new IllegalArgumentException("Plik nie zawiera poprawnych danych CSV.");
            }
            return data;
        }
    }

    private List<PortfolioData> parseStream(InputStream inputStream) {
        List<PortfolioData> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        LocalDate date = LocalDate.parse(parts[0].trim());
                        double value = Double.parseDouble(parts[1].trim());
                        dataList.add(new PortfolioData(date, value));
                    } catch (Exception e) {
                        System.err.println("Pominięto błędną linię: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd ładowania danych: " + e.getMessage());
        }
        return dataList;
    }
}