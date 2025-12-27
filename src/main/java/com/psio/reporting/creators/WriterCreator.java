package com.psio.reporting.creators;

import java.io.IOException;
import java.io.Writer;

public interface WriterCreator {
    Writer createWriter() throws IOException;
}