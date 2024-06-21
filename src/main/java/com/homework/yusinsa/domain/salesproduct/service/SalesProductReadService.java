package com.homework.yusinsa.domain.salesproduct.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductDto;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductDtoMapper;
import com.homework.yusinsa.repository.salesproduct.SalesProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.homework.yusinsa.common.Constant.SALES_PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesProductReadService {

    private final SalesProductRepository salesProductRepository;

    public SalesProductDto getSalesProductById(final long salesProductId) {
        return salesProductRepository.findById(salesProductId)
                .map(SalesProductDtoMapper::createSalesProductDtoFrom)
                .orElseThrow(() -> ApiException.makeNotFound(SALES_PRODUCT_NOT_FOUND));
    }

    public List<SalesProductDto> getSalesProducts() {
        return salesProductRepository.findAll().stream()
                .map(SalesProductDtoMapper::createSalesProductDtoFrom)
                .toList();
    }

}