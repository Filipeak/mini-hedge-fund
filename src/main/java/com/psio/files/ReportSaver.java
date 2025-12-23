package com.psio.files;

import com.psio.reporting.ReportMetrics;

public interface ReportSaver {
    void saveReport(ReportMetrics metrics);
}