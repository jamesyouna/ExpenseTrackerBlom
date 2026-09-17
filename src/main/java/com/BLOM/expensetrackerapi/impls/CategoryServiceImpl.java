package com.BLOM.expensetrackerapi.impls;

import com.BLOM.expensetrackerapi.dto.CategoryDTO;
import com.BLOM.expensetrackerapi.dto.UserDTO;
import com.BLOM.expensetrackerapi.entity.CategoryEntity;
import com.BLOM.expensetrackerapi.entity.Expense;
import com.BLOM.expensetrackerapi.entity.User;
import com.BLOM.expensetrackerapi.exceptions.ItemExistsException;
import com.BLOM.expensetrackerapi.exceptions.ResourceNotFoundException;
import com.BLOM.expensetrackerapi.repository.CategoryRepository;
import com.BLOM.expensetrackerapi.repository.ExpenseRepository;
import com.BLOM.expensetrackerapi.service.CategoryService;
import com.BLOM.expensetrackerapi.service.ExpenseService;
import com.BLOM.expensetrackerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final ExpenseRepository expenseRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> list = categoryRepository.findByUserId(userService.getLoggedInUser().getId());
        return list.stream().map(categoryEntity -> mapToDTO(categoryEntity)).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        boolean isCategoryPresent= categoryRepository.existsByNameAndUserId(categoryDTO.getName(), userService.getLoggedInUser().getId());
       if (isCategoryPresent) {
            throw new ItemExistsException("Category already exists");
        }
        CategoryEntity categoryEntity = mapToEntity(categoryDTO);

        categoryEntity.setCategoryId(UUID.randomUUID().toString());

        categoryEntity.setUser(userService.getLoggedInUser());

        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);

        return mapToDTO(savedCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(String categoryId) {

        User user = userService.getLoggedInUser();

        CategoryEntity category =
                categoryRepository
                        .findByCategoryIdAndUserId(categoryId, user.getId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Category not found"
                                )
                        );

        List<Expense> expenses =
                expenseRepository
                        .findByUserIdAndCategory_CategoryId(
                                user.getId(),
                                categoryId
                        );

        expenseRepository.deleteAll(expenses);

        categoryRepository.delete(category);
    }
    /**
     * Mapper method to convert category entity to category DTO
     * @param categoryEntity
     * @return CategoryDTO
     * */
    private CategoryDTO mapToDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .categoryId(categoryEntity.getCategoryId())
                .name(categoryEntity.getName())
                .description(categoryEntity.getDescription())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .build();
    }
    /**
     * Mapper method to convert category DTO to category entity
     * @param categoryDTO
     * @return CategoryEntity
     * */
    private CategoryEntity mapToEntity(CategoryDTO categoryDTO) {
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .categoryId(categoryDTO.getCategoryId())
                .build();
    }
    /**
     * Mapper method to convert user entity to user DTO
     * @param user
     * @return UserDTO
     * */
    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}