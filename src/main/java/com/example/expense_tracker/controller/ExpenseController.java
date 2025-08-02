package com.example.expense_tracker.controller;

import com.example.expense_tracker.dto.MonthlyExpenseReport;
import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.service.ExpenseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}")
    public Expense getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense) {
        return expenseService.createExpense(expense);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense expenseDetails) {
        return expenseService.updateExpense(id, expenseDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "Expense deleted successfully!";
    }
    @GetMapping("/report/monthly")
    public MonthlyExpenseReport getMonthlyReport(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(required = false) Double budget) {
        return expenseService.getMonthlyReport(month, year, budget);
    }

    // Get total spent for a year
    @GetMapping("/total/year")
    public Double getTotalSpentForYear(@RequestParam int year) {
        return expenseService.getTotalSpentInYear(year);
    }

    // Get total spent for a month in a year
    @GetMapping("/total/month")
    public Double getTotalSpentForMonth(
            @RequestParam int month,
            @RequestParam int year) {
        return expenseService.getTotalSpentInMonth(month, year);
    }

    @GetMapping("/search")
    public Page<Expense> searchExpenses(
                @RequestParam(required = false) String category,
                @RequestParam(required = false) String keyword,
                @RequestParam(required = false) String startDate,
                @RequestParam(required = false) String endDate,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "date,desc") String sort
        ) {
            String[] sortParams = sort.split(",");
            Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);

            Pageable pageable = PageRequest.of(page, size, sortOrder);

            LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
            LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

            return expenseService.searchExpenses(category, keyword, start, end, pageable);
        }
    }




