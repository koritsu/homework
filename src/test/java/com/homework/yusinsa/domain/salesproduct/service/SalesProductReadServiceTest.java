package com.homework.yusinsa.domain.salesproduct.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductDto;
import com.homework.yusinsa.repository.brand.entity.Brand;
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

import java.util.List;
import java.util.Optional;

import static com.homework.yusinsa.common.Constant.SALES_PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SalesProductReadServiceTest {

    @InjectMocks
    private SalesProductReadService salesProductReadService;

    @Mock
    private SalesProductRepository salesProductRepository;

    @BeforeEach
    void setUp() {
        salesProductReadService = new SalesProductReadService(salesProductRepository);
    }

    @Test
    void getSalesProductById() {
        Brand brand = Brand.builder().id(1L).name("Brand 1").build();
        Category category = Category.builder().id(1L).name("Category 1").build();
        SalesProduct salesProduct = SalesProduct.builder()
                .id(1L)
                .brand(brand)
                .category(category)
                .price(1000)
                .build();

        when(salesProductRepository.findById(1L)).thenReturn(Optional.of(salesProduct));

        SalesProductDto result = salesProductReadService.getSalesProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Brand 1", result.getBrand().getName());
        assertEquals("Category 1", result.getCategory().getName());
        assertEquals(1000, result.getPrice());
    }

    @Test
    void getSalesProductById_NotFound() {
        when(salesProductRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> salesProductReadService.getSalesProductById(1L));

        assertEquals(SALES_PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getSalesProducts() {
        Brand brand1 = Brand.builder().id(1L).name("Brand 1").build();
        Category category1 = Category.builder().id(1L).name("Category 1").build();
        SalesProduct salesProduct1 = SalesProduct.builder()
                .id(1L)
                .brand(brand1)
                .category(category1)
                .price(1000)
                .build();

        Brand brand2 = Brand.builder().id(2L).name("Brand 2").build();
        Category category2 = Category.builder().id(2L).name("Category 2").build();
        SalesProduct salesProduct2 = SalesProduct.builder()
                .id(2L)
                .brand(brand2)
                .category(category2)
                .price(2000)
                .build();

        when(salesProductRepository.findAll()).thenReturn(List.of(salesProduct1, salesProduct2));

        List<SalesProductDto> result = salesProductReadService.getSalesProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Brand 1", result.get(0).getBrand().getName());
        assertEquals("Category 1", result.get(0).getCategory().getName());
        assertEquals(1000, result.get(0).getPrice());

        assertEquals("Brand 2", result.get(1).getBrand().getName());
        assertEquals("Category 2", result.get(1).getCategory().getName());
        assertEquals(2000, result.get(1).getPrice());

    }

}
