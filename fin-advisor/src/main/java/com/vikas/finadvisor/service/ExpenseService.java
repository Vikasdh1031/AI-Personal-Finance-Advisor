package com.vikas.finadvisor.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * ExpenseService handles business logic like summarizing expenses,
 * generating insights, and preparing monthly statistics.
 */
@Service
public class ExpenseService {

    /**
     * Generate smart insights about user spending patterns.
     */
    public List<String> generateInsights() {
        List<String> insights = new ArrayList<>();

        Map<String, BigDecimal> categoryTotals = getCategorySummary();
        BigDecimal totalSpent = categoryTotals.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalSpent.compareTo(BigDecimal.ZERO) == 0) {
            insights.add("No expenses found yet. Add some to get insights!");
            return insights;
        }

        // Find top spending category
        Map.Entry<String, BigDecimal> top = categoryTotals.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (top != null) {
            BigDecimal topPercent = top.getValue()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalSpent, 2, BigDecimal.ROUND_HALF_UP);

            insights.add("Your highest spending category is **" + top.getKey()
                    + "** (" + topPercent + "% of total).");
        }

        // Detect unbalanced spending
        for (var entry : categoryTotals.entrySet()) {
            BigDecimal percent = entry.getValue()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalSpent, 2, BigDecimal.ROUND_HALF_UP);
            if (percent.compareTo(BigDecimal.valueOf(50)) > 0) {
                insights.add("⚠️ You spent more than 50% on " + entry.getKey()
                        + ". Try to balance your expenses.");
            }
        }

        // Simple saving tip
        insights.add("💡 Tip: Save at least 20% of your income each month!");

        return insights;
    }

    /**
     * Returns total spending per category.
     * Currently dummy data; will connect to DB later.
     */
    public Map<String, BigDecimal> getCategorySummary() {
        Map<String, BigDecimal> summary = new LinkedHashMap<>();
        summary.put("Food", BigDecimal.valueOf(2500));
        summary.put("Transport", BigDecimal.valueOf(800));
        summary.put("Shopping", BigDecimal.valueOf(1200));
        summary.put("Bills", BigDecimal.valueOf(1500));
        return summary;
    }

    /**
     * Returns monthly spending summary.
     * Currently dummy data; later will fetch from DB.
     */
    public Map<String, BigDecimal> getMonthlySummary() {
        Map<String, BigDecimal> summary = new LinkedHashMap<>();
        summary.put("January", BigDecimal.valueOf(7500));
        summary.put("February", BigDecimal.valueOf(6200));
        summary.put("March", BigDecimal.valueOf(8100));
        summary.put("April", BigDecimal.valueOf(6700));
        return summary;
    }
}
