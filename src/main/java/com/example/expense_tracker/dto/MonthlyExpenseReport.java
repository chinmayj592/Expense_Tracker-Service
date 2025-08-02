
package com.example.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class MonthlyExpenseReport {

    private String month; // Example: "August 2025"
    private Double totalSpent; // Total amount spent in the given month
    private Map<String, Double> categoryBreakdown; // { "Food": 1200.50, "Rent": 15000 }
    private String highestSpendingCategory; // Example: "Rent"
    private Double monthlyBudget; // Example: 25000.00
    private Double remainingBudget; // monthlyBudget - totalSpent
}
