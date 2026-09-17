package com.BLOM.expensetrackerapi.controller;

import com.BLOM.expensetrackerapi.dto.CategoryDTO;
import com.BLOM.expensetrackerapi.io.CategoryRequest;
import com.BLOM.expensetrackerapi.io.CategoryResponse;
import com.BLOM.expensetrackerapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * This controller is for managing the categories
 * @author younaj
 * **/
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    /**
     * API for creating the category
     * @param categoryRequest
     * @return categoryResonse
     * **/
    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO = mapToDTO(categoryRequest);
        CategoryDTO savedCategory = categoryService.saveCategory(categoryDTO);
        return mapToResponse(savedCategory);
    }

    /**
     * API for reading the categories
     * @reutrn list
     * **/
    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories()
                .stream()
                .map(categoryDTO -> mapToResponse(categoryDTO))
                .collect(Collectors.toList());
    }

    /**
     * API for deleting the category
     * @param categoryId
     * **/
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    /**
     * Mapper method for converting DTO object to Response object
     * @param categoryDTO
     * @return categpryResponse
     * **/
    private CategoryResponse mapToResponse(CategoryDTO categoryDTO) {
        return CategoryResponse.builder()
                .categoryId(categoryDTO.getCategoryId())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .createdAt(categoryDTO.getCreatedAt())
                .updatedAt(categoryDTO.getUpdatedAt())
                .build();
    }

    /**
     * Mapper method for converting Request object to DTO object
     * @param categoryRequest
     * @return categpryDTO
     * **/
    private CategoryDTO mapToDTO(CategoryRequest categoryRequest) {
        return CategoryDTO.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();
    }
}