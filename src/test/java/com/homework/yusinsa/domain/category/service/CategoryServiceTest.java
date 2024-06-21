package com.homework.yusinsa.domain.category.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.category.service.dto.CategoryDto;
import com.homework.yusinsa.domain.category.service.dto.CategorySaveDto;
import com.homework.yusinsa.repository.category.CategoryRepository;
import com.homework.yusinsa.repository.category.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.homework.yusinsa.common.Constant.CATEGORY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void createCategory() {
        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("New Category")
                .build();

        Category savedCategory = Category.builder().id(1L).name("New Category").build();

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDto result = categoryService.createCategory(categorySaveDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Category", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getCategories() {
        Category category1 = Category.builder().id(1L).name("Category 1").build();
        Category category2 = Category.builder().id(2L).name("Category 2").build();

        when(categoryRepository.findAllByOrderById()).thenReturn(List.of(category1, category2));

        List<CategoryDto> result = categoryService.getCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Category 1", result.get(0).getName());
        assertEquals("Category 2", result.get(1).getName());
        verify(categoryRepository, times(1)).findAllByOrderById();
    }

    @Test
    void getCategoryById() {
        Category category = Category.builder().id(1L).name("Category").build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Category", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void getCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> categoryService.getCategoryById(1L));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
    }

    @Test
    void updateCategory() {
        Category category = Category.builder().id(1L).name("Old Name").build();
        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("Updated Name")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.updateCategory(1L, categorySaveDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void updateCategory_NotFound() {
        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("Updated Name")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> categoryService.updateCategory(1L, categorySaveDto));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
    }

    @Test
    void deleteCategoryById() {
        Category category = Category.builder().id(1L).name("Category").build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(1L);
    }

    @Test
    void deleteCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> categoryService.deleteCategoryById(1L));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
    }

}
