package com.BLOM.expensetrackerapi.repository;

import com.BLOM.expensetrackerapi.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUserIdAndCategory_Name(Long userId, String category, Pageable page);

    Page<Expense> findByUserIdAndNameContaining(Long userId, String name, Pageable page);

    Page<Expense> findByUserIdAndDateBetween(Long userId, Date startDate, Date endDate, Pageable pageable);

    Page<Expense> findByUserId(Long userId, Pageable page);

    Optional<Expense> findByUserIdAndExpenseId(Long userId, String expenseId);

    List<Expense> findByUserIdAndCategory_CategoryId(Long userId, String categoryId);
}