package com.homework.yusinsa.domain.salesproduct.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductDto;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductSaveDto;
import com.homework.yusinsa.repository.brand.BrandRepository;
import com.homework.yusinsa.repository.brand.entity.Brand;
import com.homework.yusinsa.repository.category.CategoryRepository;
import com.homework.yusinsa.repository.category.entity.Category;
import com.homework.yusinsa.repository.salesproduct.SalesProductRepository;
import com.homework.yusinsa.repository.salesproduct.entity.SalesProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.homework.yusinsa.common.Constant.BRAND_NOT_FOUND;
import static com.homework.yusinsa.common.Constant.CATEGORY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SalesProductCreateServiceTest {

    @InjectMocks
    private SalesProductCreateService salesProductCreateService;

    @Mock
    private SalesProductRepository salesProductRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        salesProductCreateService = new SalesProductCreateService(salesProductRepository, brandRepository, categoryRepository);
    }

    @Test
    void createSalesProduct() {
        Brand brand = Brand.builder().id(1L).name("Brand 1").build();
        Category category = Category.builder().id(1L).name("Category 1").build();
        SalesProduct salesProduct = SalesProduct.builder()
                .id(1L)
                .brand(brand)
                .category(category)
                .price(1000)
                .build();

        SalesProductSaveDto salesProductSaveDto = SalesProductSaveDto.builder()
                .brand(SalesProductSaveDto.Brand.builder().id(1L).build())
                .category(SalesProductSaveDto.Category.builder().id(1L).build())
                .price(1000)
                .build();

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(salesProductRepository.existsSalesProductByBrand_IdAndCategory_Id(1L, 1L)).thenReturn(false);
        when(salesProductRepository.save(any(SalesProduct.class))).thenReturn(salesProduct);

        SalesProductDto result = salesProductCreateService.createSalesProduct(salesProductSaveDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Brand 1", result.getBrand().getName());
        assertEquals("Category 1", result.getCategory().getName());
        assertEquals(1000, result.getPrice());
    }

    @Test
    void createSalesProduct_Duplicate() {
        SalesProductSaveDto salesProductSaveDto = SalesProductSaveDto.builder()
                .brand(SalesProductSaveDto.Brand.builder().id(1L).build())
                .category(SalesProductSaveDto.Category.builder().id(1L).build())
                .price(1000)
                .build();

        when(salesProductRepository.existsSalesProductByBrand_IdAndCategory_Id(1L, 1L)).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> salesProductCreateService.createSalesProduct(salesProductSaveDto));

        assertTrue(exception.getMessage().contains("SalesProduct already exists. brandId: 1, categoryId: 1"));
    }

    @Test
    void createSalesProduct_BrandNotFound() {
        SalesProductSaveDto salesProductSaveDto = SalesProductSaveDto.builder()
                .brand(SalesProductSaveDto.Brand.builder().id(1L).build())
                .category(SalesProductSaveDto.Category.builder().id(1L).build())
                .price(1000)
                .build();

        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> salesProductCreateService.createSalesProduct(salesProductSaveDto));

        assertEquals(BRAND_NOT_FOUND, exception.getMessage());
    }

    @Test
    void createSalesProduct_CategoryNotFound() {
        SalesProductSaveDto salesProductSaveDto = SalesProductSaveDto.builder()
                .brand(SalesProductSaveDto.Brand.builder().id(1L).build())
                .category(SalesProductSaveDto.Category.builder().id(1L).build())
                .price(1000)
                .build();

        when(brandRepository.findById(1L)).thenReturn(Optional.of(Brand.builder().id(1L).build()));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> salesProductCreateService.createSalesProduct(salesProductSaveDto));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
    }

}
