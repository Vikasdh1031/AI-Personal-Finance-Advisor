package com.vikas.finadvisor.controller;

import com.vikas.finadvisor.entity.Expense;
import com.vikas.finadvisor.repo.ExpenseRepo;
import com.vikas.finadvisor.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepo repo;
    private final ExpenseService service;

    // Constructor for dependency injection
    public ExpenseController(ExpenseRepo repo, ExpenseService service) {
        this.repo = repo;
        this.service = service;
    }

    // ✅ CREATE EXPENSE
    @PostMapping
    public Expense create(@Valid @RequestBody Expense expense) {
        if (expense.getCurrency() == null || expense.getCurrency().isBlank()) {
            expense.setCurrency("INR");
        }
        if (expense.getSpentOn() == null) {
            expense.setSpentOn(LocalDate.now());
        }
        return repo.save(expense);
    }

    // ✅ LIST ALL EXPENSES
    @GetMapping
    public List<Expense> listAll() {
        return repo.findAll();
    }

    // ✅ TEST BUILDER ENDPOINT
    @GetMapping("/test")
    public Expense testExpense() {
        return Expense.builder()
                .amount(BigDecimal.valueOf(100))
                .category("Test")
                .currency("INR")
                .spentOn(LocalDate.now())
                .note("Builder test")
                .build();
    }

    // ✅ SUMMARY — TOTAL BY CATEGORY
    @GetMapping("/summary/category")
    public Map<String, Object> categorySummary() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summaryType", "Category Totals");
        result.put("data", service.getCategorySummary());
        return result;
    }

    // ✅ SUMMARY — TOTAL BY MONTH
    @GetMapping("/summary/monthly")
    public Map<String, Object> monthlySummary() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summaryType", "Monthly Totals");
        result.put("data", service.getMonthlySummary());
        return result;
    }

    // ✅ SMART INSIGHTS (AI-Like Advice)
    @GetMapping("/insights")
    public Map<String, Object> getInsights() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summaryType", "AI Insights");
        result.put("insights", service.generateInsights());
        return result;
    }
}
