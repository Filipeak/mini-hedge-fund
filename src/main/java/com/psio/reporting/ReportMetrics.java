package com.psio.reporting;

import java.util.Locale;

public record ReportMetrics(
        double ror,
        double maxDrawdown,
        double winRate,
        double finalBalance
) {
    @Override
    public String toString() {
        return "=== RAPORT SYMULACJI ===\n\n" +
                String.format(Locale.US, "Finalny Balans:   %.2f PLN", finalBalance) + "\n" +
                String.format(Locale.US, "Zwrot (ROR):      %.2f %%", ror) + "\n\n" +
                String.format(Locale.US, "Max Drawdown:     %.2f %%", maxDrawdown) + "\n" +
                String.format(Locale.US, "Win Rate:         %.2f %%", winRate) + "\n\n" +
                "========================";
    }
}