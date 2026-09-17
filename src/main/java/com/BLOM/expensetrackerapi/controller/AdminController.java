package com.BLOM.expensetrackerapi.controller;

import com.BLOM.expensetrackerapi.entity.Expense;
import com.BLOM.expensetrackerapi.entity.User;
import com.BLOM.expensetrackerapi.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/users/search")
    public List<User> searchUsersByEmail(@RequestParam String email) {
        return adminService.searchUsersByEmail(email);
    }
    @GetMapping("/expenses")
    public List<Expense> getAllExpenses() {
        return adminService.getAllExpenses();
    }
    @GetMapping("/users/{userId}/expenses")
    public List<Expense> getExpensesByUserId(@PathVariable Long userId) {
        return adminService.getExpensesByUserId(userId);
    }

    @GetMapping("/expenses/total")
    public BigDecimal getTotalExpenses() {
        return adminService.getTotalExpenses();
    }


    // for admins to delete users
    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
    }



}