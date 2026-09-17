package com.BLOM.expensetrackerapi.impls;

import com.BLOM.expensetrackerapi.dto.CategoryDTO;
import com.BLOM.expensetrackerapi.dto.ExpenseDTO;
import com.BLOM.expensetrackerapi.entity.CategoryEntity;
import com.BLOM.expensetrackerapi.entity.Expense;
import com.BLOM.expensetrackerapi.exceptions.ResourceNotFoundException;
import com.BLOM.expensetrackerapi.repository.CategoryRepository;
import com.BLOM.expensetrackerapi.repository.ExpenseRepository;
import com.BLOM.expensetrackerapi.service.CurrencyService;
import com.BLOM.expensetrackerapi.service.ExpenseService;
import com.BLOM.expensetrackerapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service //create an object and manage it
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired //dependency injection
    private ExpenseRepository expenseRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CurrencyService currencyService;


    @Override
    public ExpenseDTO getExpenseById(String expenseId) {

        Expense expense = getExpenseEntityById(expenseId);

        return mapToDTO(expense);
    }


    private Expense getExpenseEntityById(String expenseId) {

        return expenseRepo.findByUserIdAndExpenseId(
                userService.getLoggedInUser().getId(),
                expenseId
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Expense is not found for the id " + expenseId
                )
        );
    }


    @Override
    public void deleteExpenseById(String expenseId) {

        Expense expense = getExpenseEntityById(expenseId);

        expenseRepo.delete(expense);
    }


    @Override
    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {

        //CHECK THE EXISTANCE OF CATEGORY
        Optional<CategoryEntity> optionalCategory = categoryRepository.findByCategoryIdAndUserId(expenseDTO.getCategoryId(), userService.getLoggedInUser().getId());

        if (!optionalCategory.isPresent()) {
            throw new ResourceNotFoundException(
                    "Category is not found for the id " + expenseDTO.getCategoryId()
            );
        }

        //VALIDATE THE CURRENCY
        if (expenseDTO.getCurrency() == null || (!expenseDTO.getCurrency().equalsIgnoreCase("USD") && expenseDTO.getCurrency().equalsIgnoreCase("LBP"))) {
            throw new IllegalArgumentException(
                    "Currency must be either USD or LBP"
            );
        }

        //CHECK THE CURRENCY
        //All expenses are stored in USD in the database
        //If the user enters the amount in LBP, convert it to USD before saving
        if (expenseDTO.getCurrency().equalsIgnoreCase("LBP")) {

            expenseDTO.setAmount(
                    currencyService.convertLbpToUsd(expenseDTO.getAmount())
            );
        }

        //GENERATE A UNIQUE EXPENSE ID
        expenseDTO.setExpenseId(UUID.randomUUID().toString());

        //map to entity object
        Expense newExpense = mapToEntity(expenseDTO);

        //set category and logged in user
        newExpense.setCategory(optionalCategory.get());
        newExpense.setUser(userService.getLoggedInUser());

        //save to database
        newExpense = expenseRepo.save(newExpense);

        //map to DTO
        return mapToDTO(newExpense);
    }


    @Override
    public ExpenseDTO updateExpenseDetails(String expenseId, ExpenseDTO expenseDTO) {

        Expense existingExpense = getExpenseEntityById(expenseId);
        existingExpense.setName(expenseDTO.getName() != null ? expenseDTO.getName() : existingExpense.getName());

        existingExpense.setDescription(expenseDTO.getDescription() != null ? expenseDTO.getDescription() : existingExpense.getDescription());
        existingExpense.setType(expenseDTO.getType() != null ? expenseDTO.getType() : existingExpense.getType());

        //UPDATE THE AMOUNT
        if (expenseDTO.getAmount() != null) {

            //VALIDATE THE CURRENCY
            if (expenseDTO.getCurrency() == null || (!expenseDTO.getCurrency().equalsIgnoreCase("USD") && !expenseDTO.getCurrency().equalsIgnoreCase("LBP"))) {
                throw new IllegalArgumentException(
                        "Currency must be either USD or LBP"
                );
            }

            //If the amount was entered in LBP, convert it to USD before saving
            if (expenseDTO.getCurrency().equalsIgnoreCase("LBP")) {

                existingExpense.setAmount(
                        currencyService.convertLbpToUsd(
                                expenseDTO.getAmount()
                        )
                );

            } else {

                //If the amount was entered in USD, save it normally
                existingExpense.setAmount(
                        expenseDTO.getAmount()
                );
            }
        }

        existingExpense.setDate(
                expenseDTO.getDate() != null
                        ? expenseDTO.getDate()
                        : existingExpense.getDate()
        );

        if (expenseDTO.getCategoryId() != null) {

            CategoryEntity category =
                    categoryRepository
                            .findByCategoryIdAndUserId(
                                    expenseDTO.getCategoryId(),
                                    userService.getLoggedInUser().getId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Category is not found for the id "
                                                    + expenseDTO.getCategoryId()
                                    )
                            );

            existingExpense.setCategory(category);
        }

        Expense updatedExpense =
                expenseRepo.save(existingExpense);

        return mapToDTO(updatedExpense);
    }




    @Override
    public List<ExpenseDTO> getAllExpenses(Pageable page) {
        List<Expense> listOfExpenses = expenseRepo.findByUserId(userService.getLoggedInUser().getId(), page).toList();

        return listOfExpenses.stream().map(this::mapToDTO).collect(Collectors.toList());
    }





    @Override
    public List<ExpenseDTO> readByCategory(String category, Pageable page) {
        List<Expense> expenses = expenseRepo.findByUserIdAndCategory_Name(userService.getLoggedInUser().getId(), category, page).toList();

        return expenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<ExpenseDTO> readByName(String keyword, Pageable page) {
        List<Expense> expenses = expenseRepo.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(), keyword, page).toList();

        return expenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<ExpenseDTO> readByDate(
            Date startDate,
            Date endDate,
            Pageable page) {

        if (startDate == null) {
            startDate = new Date(0);
        }

        if (endDate == null) {
            endDate = new Date(System.currentTimeMillis());
        }

        List<Expense> expenses =
                expenseRepo.findByUserIdAndDateBetween(
                        userService.getLoggedInUser().getId(),
                        startDate,
                        endDate,
                        page
                ).toList();

        return expenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private ExpenseDTO mapToDTO(Expense expense) {

        return ExpenseDTO.builder()
                .expenseId(expense.getExpenseId())
                .name(expense.getName())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .type(expense.getType() != null ? expense.getType() : com.BLOM.expensetrackerapi.entity.TransactionType.WITHDRAW)
                .date(expense.getDate())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .categoryDTO(mapToCategoryDTO(expense.getCategory()))
                .build();
    }


    private CategoryDTO mapToCategoryDTO(CategoryEntity category) {

        return CategoryDTO.builder()
                .name(category.getName())
                .categoryId(category.getCategoryId())
                .build();
    }


    private Expense mapToEntity(ExpenseDTO expenseDTO) {

        return Expense.builder()
                .expenseId(expenseDTO.getExpenseId())
                .name(expenseDTO.getName())
                .description(expenseDTO.getDescription())
                .date(expenseDTO.getDate())
                .amount(expenseDTO.getAmount())
                .type(expenseDTO.getType())
                .build();
    }
}