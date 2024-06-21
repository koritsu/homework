package com.homework.yusinsa.domain.category.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.category.service.dto.CategoryDto;
import com.homework.yusinsa.domain.category.service.dto.CategorySaveDto;
import com.homework.yusinsa.repository.category.CategoryRepository;
import com.homework.yusinsa.repository.category.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.homework.yusinsa.common.Constant.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto createCategory(final CategorySaveDto categorySaveDto) {

        return CategoryDtoMapper.createCategoryDtoFrom(
                categoryRepository.save(
                        CategoryDtoMapper.createCategoryFrom(categorySaveDto)
                )
        );

    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAllByOrderById().stream()
                .map(CategoryDtoMapper::createCategoryDtoFrom)
                .toList();
    }


    public CategoryDto getCategoryById(final long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(CategoryDtoMapper::createCategoryDtoFrom)
                .orElseThrow(
                        () -> ApiException.makeNotFound(CATEGORY_NOT_FOUND)
                );
    }

    @Transactional
    public CategoryDto updateCategory(final long categoryId, final CategorySaveDto categorySaveDto) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> ApiException.makeNotFound(CATEGORY_NOT_FOUND)
                );

        category.updateName(categorySaveDto.getName());

        return CategoryDtoMapper.createCategoryDtoFrom(category);

    }

    public void deleteCategoryById(final long categoryId) {

        categoryRepository.findById(categoryId).ifPresentOrElse(
                categoryRepository::delete,
                () -> {
                    throw ApiException.makeNotFound(CATEGORY_NOT_FOUND);
                }
        );

    }

    /**
     * CategoryDtoMapper
     */

    private static class CategoryDtoMapper {

        public static CategoryDto createCategoryDtoFrom(final Category category) {
            return CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        }

        public static Category createCategoryFrom(final CategorySaveDto categorySaveDto) {
            return Category.builder()
                    .name(categorySaveDto.getName())
                    .build();
        }

    }


}