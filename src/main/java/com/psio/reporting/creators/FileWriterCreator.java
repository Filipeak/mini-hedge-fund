package com.psio.reporting.creators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileWriterCreator implements WriterCreator {
    private final String filePath;

    public FileWriterCreator(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Writer createWriter() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        System.out.println("Report file opened: " + filePath);

        return writer;
    }
}