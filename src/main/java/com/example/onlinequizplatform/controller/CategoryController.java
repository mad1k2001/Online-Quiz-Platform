package com.example.onlinequizplatform.controller;

import com.example.onlinequizplatform.dto.CategoryDto;
import com.example.onlinequizplatform.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getCategory() {
        return ResponseEntity.ok(categoryService.getCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("add")
    public ResponseEntity<CategoryDto> createCategory(Authentication authentication, @RequestBody @Valid CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/edit/category/{categoryId}")
    public HttpStatus updateCategory(Authentication authentication, @PathVariable Long categoryId, @RequestBody CategoryDto categoryDto){
        categoryService.updateCategory(categoryDto, authentication, categoryId);
        return HttpStatus.OK;
    }}
