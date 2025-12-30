package com.psio.reporting.creators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileWriterCreator implements WriterCreator {

    private static final Logger logger = LogManager.getLogger(FileWriterCreator.class);

    private final String filePath;

    public FileWriterCreator(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Writer createWriter() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        logger.info("Report file opened: {}", filePath);

        return writer;
    }
}