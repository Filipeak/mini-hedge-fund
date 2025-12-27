package com.psio.reporting;

public record ReportMetrics(
        double ror,
        double maxDrawdown,
        double winRate,
        double finalBalance
) {
    @Override
    public String toString() {
        return "=== RAPORT SYMULACJI ===\n\n" +
                String.format("Finalny Balans:   %.2f PLN", finalBalance) + "\n" +
                String.format("Zwrot (ROR):      %.2f %%", ror) + "\n\n" +
                String.format("Max Drawdown:     %.2f %%", maxDrawdown) + "\n" +
                String.format("Win Rate:         %.2f %%", winRate) + "\n\n" +
                "========================";
    }
}