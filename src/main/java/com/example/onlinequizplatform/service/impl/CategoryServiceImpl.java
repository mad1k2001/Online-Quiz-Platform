package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.CategoryDao;
import com.example.onlinequizplatform.dto.CategoryDto;
import com.example.onlinequizplatform.exeptions.CustomException;
import com.example.onlinequizplatform.models.Category;
import com.example.onlinequizplatform.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;
    @Override
    public List<CategoryDto> getCategory() {
        List<Category> categories = categoryDao.getCategory();
        return getCategoryDto(categories);
    }

    @Override
    public Optional<CategoryDto> getCategoryById(Long id) throws CustomException {
        return categoryDao.getCategoryById(id)
                .map(this::mapToDto)
                .map(Optional::of)
                .orElseThrow(() -> new CustomException("Can't find category with id: " + id));
    }

    @Override
    public Long createCategory(CategoryDto categoryDto, Authentication authentication){
        Category category = makeCategory(categoryDto);
        return categoryDao.createCategory(category);
    }

    @Override
    public void updateCategory(CategoryDto categoryDto, Authentication authentication, Long categoryId){
        Category category = makeCategory(categoryDto);
        categoryDao.updateCategory(category);
    }
    private CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private Category makeCategory(CategoryDto categoryDto){
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    private List<CategoryDto> getCategoryDto(List<Category> categories) {
        List<CategoryDto> dto = new ArrayList<>();
        categories.forEach(e -> dto.add( CategoryDto.builder()
                .id(e.getId())
                .name(e.getName())
                .build()));
        return dto;
    }
}
