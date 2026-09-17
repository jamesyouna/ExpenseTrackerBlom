package com.BLOM.expensetrackerapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // returns html through thymeleaf
public class PageController {

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    } // thymeleaf view resolver and looks inside the templates

    @GetMapping("/expenses-page")
    public String expensesPage() {
        return "expenses";
    }

    @GetMapping("/categories-page")
    public String categoriesPage() {
        return "categories";
    }

    @GetMapping("/profile-page")
    public String profilePage() {
        return "profile";
    }

    @GetMapping("/register-page")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/admin-page")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/logistics-page")
    public String logisticsPage() {
        return "logistics";
    }


    @GetMapping("/verify-page")
    public String verifyPage() {return "verify";}


}