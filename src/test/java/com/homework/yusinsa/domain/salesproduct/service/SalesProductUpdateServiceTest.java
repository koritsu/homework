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

import static com.homework.yusinsa.common.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SalesProductUpdateServiceTest {

    @InjectMocks
    private SalesProductUpdateService salesProductUpdateService;

    @Mock
    private SalesProductRepository salesProductRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        salesProductUpdateService = new SalesProductUpdateService(salesProductRepository, brandRepository, categoryRepository);
    }

    @Test
    void updateSalesProduct() {
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
                .price(2000)
                .build();

        when(salesProductRepository.findById(1L)).thenReturn(Optional.of(salesProduct));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(salesProductRepository.findFirstByBrand_IdAndCategory_Id(1L, 1L)).thenReturn(Optional.of(salesProduct));

        SalesProductDto result = salesProductUpdateService.updateSalesProduct(1L, salesProductSaveDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Brand 1", result.getBrand().getName());
        assertEquals("Category 1", result.getCategory().getName());
        assertEquals(2000, result.getPrice());
    }

    @Test
    void updateSalesProduct_SalesProductNotFound() {
        SalesProductSaveDto salesProductSaveDto = SalesProductSaveDto.builder()
                .brand(SalesProductSaveDto.Brand.builder().id(1L).build())
                .category(SalesProductSaveDto.Category.builder().id(1L).build())
                .price(2000)
                .build();

        when(salesProductRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> salesProductUpdateService.updateSalesProduct(1L, salesProductSaveDto));

        assertEquals(SALES_PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void updateSalesProduct_BrandNotFound() {
        SalesProduct salesProduct = SalesProduct.builder()
                .id(1L)
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .price(1000)
                .build();

        SalesProductSaveDto salesProductSaveDto = SalesProductSaveDto.builder()
                .brand(SalesProductSaveDto.Brand.builder().id(2L).build())
                .category(SalesProductSaveDto.Category.builder().id(1L).build())
                .price(2000)
                .build();

        when(salesProductRepository.findById(1L)).thenReturn(Optional.of(salesProduct));
        when(brandRepository.findById(2L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> salesProductUpdateService.updateSalesProduct(1L, salesProductSaveDto));

        assertEquals(BRAND_NOT_FOUND, exception.getMessage());
    }

    @Test
    void updateSalesProduct_CategoryNotFound() {
        SalesProduct salesProduct = SalesProduct.builder()
                .id(1L)
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .price(1000)
                .build();

        SalesProductSaveDto salesProductSaveDto = SalesProductSaveDto.builder()
                .brand(SalesProductSaveDto.Brand.builder().id(1L).build())
                .category(SalesProductSaveDto.Category.builder().id(2L).build())
                .price(2000)
                .build();

        when(salesProductRepository.findById(1L)).thenReturn(Optional.of(salesProduct));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(salesProduct.getBrand()));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class,
                () -> salesProductUpdateService.updateSalesProduct(1L, salesProductSaveDto));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
    }

    @Test
    void updateSalesProduct_DuplicateSalesProduct() {
        Brand brand = Brand.builder().id(1L).name("Brand 1").build();
        Category category = Category.builder().id(1L).name("Category 1").build();

        SalesProductSaveDto salesProductSaveDto = SalesProductSaveDto.builder()
                .brand(SalesProductSaveDto.Brand.builder().id(1L).build())
                .category(SalesProductSaveDto.Category.builder().id(1L).build())
                .price(2000)
                .build();

        SalesProduct anotherSalesProduct = SalesProduct.builder()
                .id(2L)
                .brand(brand)
                .category(category)
                .price(1500)
                .build();

        when(salesProductRepository.findFirstByBrand_IdAndCategory_Id(1L, 1L)).thenReturn(Optional.of(anotherSalesProduct));

        ApiException exception = assertThrows(ApiException.class, () -> salesProductUpdateService.updateSalesProduct(1L, salesProductSaveDto));

        assertEquals(String.format(SALES_PRODUCT_FORMAT_ALREADY_EXISTS, 1L, 1L), exception.getMessage());

    }

}
