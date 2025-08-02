package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT e.category, SUM(e.amount) FROM expenses e WHERE MONTH(e.date) = :month AND YEAR(e.date) = :year GROUP BY e.category")
    List<Object[]> getMonthlyCategoryTotals(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(e.amount) FROM expenses e WHERE MONTH(e.date) = :month AND YEAR(e.date) = :year")
    Double getTotalSpentInMonth(@Param("month") int month, @Param("year") int year);

    // Total spent for a given year
    @Query("SELECT SUM(e.amount) FROM expenses e WHERE YEAR(e.date) = :year")
    Double getTotalSpentInYear(@Param("year") int year);

    @Query("SELECT e FROM expenses e " +
            "WHERE (:category IS NULL OR e.category = :category) " +
            "AND (:keyword IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:startDate IS NULL OR e.date >= :startDate) " +
            "AND (:endDate IS NULL OR e.date <= :endDate)")
    Page<Expense> searchExpenses(
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );


}
