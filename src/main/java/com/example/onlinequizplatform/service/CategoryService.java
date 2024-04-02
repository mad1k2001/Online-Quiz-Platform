package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.CategoryDto;
import com.example.onlinequizplatform.exeptions.CustomException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> getCategory();
    Optional<CategoryDto> getCategoryById(Long id) throws CustomException;
    Long createCategory(CategoryDto categoryDto, Authentication authentication);
    void updateCategory(CategoryDto categoryDto, Authentication authentication, Long categoryId);
}
