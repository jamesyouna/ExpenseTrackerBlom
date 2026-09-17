package com.BLOM.expensetrackerapi.dto;

import com.BLOM.expensetrackerapi.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDTO {

    private String expenseId;
    private String name;
    private String description;
    private BigDecimal amount;
    private String currency;

    private TransactionType type;

    private Date date;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private CategoryDTO categoryDTO;
    private UserDTO userDTO;
    private String categoryId;
}
