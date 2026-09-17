package com.BLOM.expensetrackerapi.impls;

import com.BLOM.expensetrackerapi.entity.Expense;
import com.BLOM.expensetrackerapi.entity.User;
import com.BLOM.expensetrackerapi.repository.ExpenseRepository;
import com.BLOM.expensetrackerapi.repository.UserRepository;
import com.BLOM.expensetrackerapi.service.AdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.BLOM.expensetrackerapi.entity.Role;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public AdminServiceImpl(UserRepository userRepository,
                            ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> searchUsersByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
    @Override
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId, Pageable.unpaged()).getContent();
    }

    @Override
    public BigDecimal getTotalExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    //to allow admin to delete users
    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Admin accounts cannot be deleted");
        }

        userRepository.delete(user);
    }
}