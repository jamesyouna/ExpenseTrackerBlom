package com.BLOM.expensetrackerapi.repository;

import com.BLOM.expensetrackerapi.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


/**
 * JPA repository for Category Entity
 * */

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    /**
     * finder method to retrieve the categories by user id
     * @param userId
     * @return list
     * */
    List<CategoryEntity> findByUserId(Long userId);
    /**
     * finder method to fetch the categories by user id and category id
     * @param id, categpryId
     * @return optional
     * */
    Optional<CategoryEntity>  findByCategoryIdAndUserId(String categoryId, Long userId);
    /**
     * checks whether category is present or not by userId and category name
     * @param userId
     * @return Optional<CategoryEntity>
     * */
    boolean existsByNameAndUserId(String name, Long userId);
}