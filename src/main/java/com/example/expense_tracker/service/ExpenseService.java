package com.example.expense_tracker.service;

import com.example.expense_tracker.dto.MonthlyExpenseReport;
import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense expenseDetails) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            expense.setDescription(expenseDetails.getDescription());
            expense.setAmount(expenseDetails.getAmount());
            expense.setCategory(expenseDetails.getCategory());
            expense.setDate(expenseDetails.getDate());
            return expenseRepository.save(expense);
        }
        return null;
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public MonthlyExpenseReport getMonthlyReport(int month, int year, Double monthlyBudget) {
        Double totalSpent = expenseRepository.getTotalSpentInMonth(month, year);
        if (totalSpent == null) totalSpent = 0.0;

        List<Object[]> breakdownData = expenseRepository.getMonthlyCategoryTotals(month, year);
        Map<String, Double> categoryBreakdown = new HashMap<>();
        String highestCategory = null;
        Double highestAmount = 0.0;

        for (Object[] row : breakdownData) {
            String category = (String) row[0];
            Double amount = (Double) row[1];
            categoryBreakdown.put(category, amount);

            if (amount > highestAmount) {
                highestAmount = amount;
                highestCategory = category;
            }
        }

        Double remainingBudget = (monthlyBudget != null) ? (monthlyBudget - totalSpent) : null;

        return new MonthlyExpenseReport(
                YearMonth.of(year, month).getMonth().toString() + " " + year,
                totalSpent,
                categoryBreakdown,
                highestCategory,
                monthlyBudget,
                remainingBudget
        );
    }

    public Double getTotalSpentInYear(int year) {
        Double total = expenseRepository.getTotalSpentInYear(year);
        return (total != null) ? total : 0.0;
    }

    public Double getTotalSpentInMonth(int month, int year) {
        Double total = expenseRepository.getTotalSpentInMonth(month, year);
        return (total != null) ? total : 0.0;
    }

    public Page<Expense> searchExpenses(String category, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return expenseRepository.searchExpenses(category, keyword, startDate, endDate, pageable);
    }
}
