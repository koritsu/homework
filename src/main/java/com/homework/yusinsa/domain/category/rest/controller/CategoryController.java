package com.homework.yusinsa.domain.category.rest.controller;

import com.homework.yusinsa.domain.category.rest.dto.CategoryResponseDto;
import com.homework.yusinsa.domain.category.rest.dto.CategorySaveRequestDto;
import com.homework.yusinsa.domain.category.service.CategoryService;
import com.homework.yusinsa.domain.category.service.dto.CategoryDto;
import com.homework.yusinsa.domain.category.service.dto.CategorySaveDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CategoryController - Category CRUD API Controller
 *
 * @author khkim
 * @since 6/20/24
 **/
@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public CategoryResponseDto getCategoryById(@PathVariable final long categoryId) {
        return CategoryDtoMapper.createCategoryResponseFrom(categoryService.getCategoryById(categoryId));
    }

    @GetMapping
    public List<CategoryResponseDto> getCategories() {
        return categoryService.getCategories().stream()
                .map(CategoryDtoMapper::createCategoryResponseFrom)
                .toList();

    }

    @PostMapping
    public CategoryResponseDto createCategory(@Valid @RequestBody final CategorySaveRequestDto categorySaveRequestDto) {

        return CategoryDtoMapper.createCategoryResponseFrom(
                categoryService.createCategory(
                        CategoryDtoMapper.createCategorySaveDtoFrom(categorySaveRequestDto)
                ));
    }

    @PutMapping("/{categoryId}")
    public CategoryResponseDto updateCategory(@PathVariable final long categoryId,
                                              @Valid @RequestBody final CategorySaveRequestDto categorySaveRequestDto) {
        return CategoryDtoMapper.createCategoryResponseFrom(
                categoryService.updateCategory(categoryId, CategoryDtoMapper.createCategorySaveDtoFrom(categorySaveRequestDto))
        );
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategoryById(@PathVariable final long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }


    /**
     * CategoryDtoMapper
     */
    private static class CategoryDtoMapper {

        public static CategorySaveDto createCategorySaveDtoFrom(final CategorySaveRequestDto requestDto) {
            return CategorySaveDto.builder()
                    .name(requestDto.getName())
                    .build();
        }

        public static CategoryResponseDto createCategoryResponseFrom(final CategoryDto categoryDto) {
            return CategoryResponseDto.builder()
                    .id(categoryDto.getId())
                    .name(categoryDto.getName())
                    .build();
        }

    }

}