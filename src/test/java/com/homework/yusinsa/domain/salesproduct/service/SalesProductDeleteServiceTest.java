package com.homework.yusinsa.domain.salesproduct.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.repository.salesproduct.SalesProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static com.homework.yusinsa.common.Constant.SALES_PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SalesProductDeleteServiceTest {

    @InjectMocks
    private SalesProductDeleteService salesProductDeleteService;

    @Mock
    private SalesProductRepository salesProductRepository;

    @BeforeEach
    void setUp() {
        salesProductDeleteService = new SalesProductDeleteService(salesProductRepository);
    }

    @Test
    void deleteSalesProductById() {
        when(salesProductRepository.existsById(1L)).thenReturn(true);

        salesProductDeleteService.deleteSalesProductById(1L);
    }

    @Test
    void deleteSalesProductById_NotFound() {
        when(salesProductRepository.existsById(1L)).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> salesProductDeleteService.deleteSalesProductById(1L));

        assertEquals(SALES_PRODUCT_NOT_FOUND, exception.getMessage());
    }

}
