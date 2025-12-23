package com.psio.reporting;

public record ReportMetrics(
        double ror,
        double maxDrawdown,
        double winRate,
        double finalBalance
) {}