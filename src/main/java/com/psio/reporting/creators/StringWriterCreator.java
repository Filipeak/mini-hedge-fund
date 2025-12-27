package com.psio.reporting.creators;

import java.io.StringWriter;
import java.io.Writer;

public class StringWriterCreator implements WriterCreator {
    private StringWriter stringWriter;

    @Override
    public Writer createWriter() {
        stringWriter = new StringWriter();
        return stringWriter;
    }

    public String getData() {
        if (stringWriter != null) {
            return stringWriter.toString();
        }
        return null;
    }
}