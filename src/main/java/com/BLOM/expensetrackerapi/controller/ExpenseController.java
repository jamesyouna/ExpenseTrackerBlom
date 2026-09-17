package com.BLOM.expensetrackerapi.controller;

import com.BLOM.expensetrackerapi.dto.CategoryDTO;
import com.BLOM.expensetrackerapi.dto.ExpenseDTO;
import com.BLOM.expensetrackerapi.entity.Expense;
import com.BLOM.expensetrackerapi.io.CategoryResponse;
import com.BLOM.expensetrackerapi.io.ExpenseRequest;
import com.BLOM.expensetrackerapi.io.ExpenseResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.BLOM.expensetrackerapi.service.ExpenseService;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/expenses")
    public List<ExpenseResponse> getAllExpenses(Pageable page) {

            List<ExpenseDTO> listofExpenses =  expenseService.getAllExpenses(page);
            return listofExpenses.stream().map(expenseDTO -> mapToResponse(expenseDTO)).collect(Collectors.toList());
    }

    @GetMapping("/expenses/{expenseId}")
    public ExpenseResponse getExpenseById(@PathVariable String expenseId) {
        ExpenseDTO expenseDTO = expenseService.getExpenseById(expenseId);
        return mapToResponse(expenseDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/expenses/{expenseId}")
    public void deleteExpenseById(@PathVariable String expenseId) {
        expenseService.deleteExpenseById(expenseId);
    }

    @ResponseStatus(value= HttpStatus.CREATED)
    @PostMapping("/expenses")
    public ExpenseResponse saveExpenseDetails(@Valid @RequestBody ExpenseRequest expenseRequest) {
        ExpenseDTO expenseDTO = mapToDTO(expenseRequest);
        expenseDTO = expenseService.saveExpenseDetails(expenseDTO);
        return mapToResponse(expenseDTO);
    }

    private ExpenseResponse mapToResponse(ExpenseDTO expenseDTO) {
        return ExpenseResponse.builder()
                .expenseId(expenseDTO.getExpenseId())
                .name(expenseDTO.getName())
                .description(expenseDTO.getDescription())
                .amount(expenseDTO.getAmount())
                .type(expenseDTO.getType())
                .date(expenseDTO.getDate())
                .category(mapToCategoryResponse(expenseDTO.getCategoryDTO()))
                .createdAt(expenseDTO.getCreatedAt())
                .updatedAt(expenseDTO.getUpdatedAt())
                .build();
    }

    private CategoryResponse mapToCategoryResponse(CategoryDTO categoryDTO) {
        return CategoryResponse.builder()
                .categoryId(categoryDTO.getCategoryId())
                .name(categoryDTO.getName())
                .build();
    }

    private ExpenseDTO mapToDTO(ExpenseRequest expenseRequest) {
        return ExpenseDTO.builder()
                .name(expenseRequest.getName())
                .description(expenseRequest.getDescription())
                .amount(expenseRequest.getAmount())
                .currency(expenseRequest.getCurrency())
                .type(expenseRequest.getType())
                .categoryId(expenseRequest.getCategoryId())
                .date(expenseRequest.getDate())
                .build();
    }

    @PutMapping("/expenses/{expenseId}")
    public ExpenseResponse updateExpenseDetails(@Valid @RequestBody ExpenseRequest expenseRequest,@PathVariable String expenseId) {
        ExpenseDTO expenseDTO = mapToDTO(expenseRequest);
        expenseDTO = expenseService.updateExpenseDetails(expenseId, expenseDTO);

        return mapToResponse(expenseDTO);
    }

    @GetMapping("/expenses/category")
    public List<ExpenseResponse> getExpensesByCategory(@RequestParam String category, Pageable page) {
        return expenseService.readByCategory(category, page)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/expenses/name")
    public List<ExpenseResponse> getExpensesByName(@RequestParam String keyword, Pageable page) {
        return expenseService.readByName(keyword, page).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @GetMapping("/expenses/date")
    public List<ExpenseResponse> getExpensesByDate(@RequestParam(required = false) Date startDate, @RequestParam(required = false) Date endDate, Pageable page) {
        return expenseService.readByDate(startDate, endDate, page)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

} //created a rest endpoints
