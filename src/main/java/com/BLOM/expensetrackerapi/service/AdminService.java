package com.BLOM.expensetrackerapi.service;

import com.BLOM.expensetrackerapi.entity.Expense;
import com.BLOM.expensetrackerapi.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {

    List<User> getAllUsers();

    List<User> searchUsersByEmail(String email);

    List<Expense> getAllExpenses();

    List<Expense> getExpensesByUserId(Long userId);

    BigDecimal getTotalExpenses();

    void deleteUser(Long userId);
}