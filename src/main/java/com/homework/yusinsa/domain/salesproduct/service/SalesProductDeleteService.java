package com.homework.yusinsa.domain.salesproduct.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.repository.salesproduct.SalesProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.homework.yusinsa.common.Constant.SALES_PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesProductDeleteService {

    private final SalesProductRepository salesProductRepository;

    public void deleteSalesProductById(final long salesProductId) {

        if (!salesProductRepository.existsById(salesProductId)) {
            throw ApiException.makeNotFound(SALES_PRODUCT_NOT_FOUND);
        }
        salesProductRepository.deleteById(salesProductId);
    }

}