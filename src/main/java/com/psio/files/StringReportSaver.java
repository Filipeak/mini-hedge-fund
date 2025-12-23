package com.psio.files;

import com.psio.reporting.ReportMetrics;
import java.io.FileWriter;
import java.io.IOException;

public class StringReportSaver implements ReportSaver {

    @Override
    public void saveReport(ReportMetrics metrics) {
        String rawData = String.format("%.2f;%.2f;%.2f;%.2f",
                metrics.ror(), metrics.maxDrawdown(), metrics.winRate(), metrics.finalBalance());
        System.out.println(rawData);
    }
}