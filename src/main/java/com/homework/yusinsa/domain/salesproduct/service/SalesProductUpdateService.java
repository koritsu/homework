package com.homework.yusinsa.domain.salesproduct.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductDto;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductDtoMapper;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductSaveDto;
import com.homework.yusinsa.repository.brand.BrandRepository;
import com.homework.yusinsa.repository.brand.entity.Brand;
import com.homework.yusinsa.repository.category.CategoryRepository;
import com.homework.yusinsa.repository.category.entity.Category;
import com.homework.yusinsa.repository.salesproduct.SalesProductRepository;
import com.homework.yusinsa.repository.salesproduct.entity.SalesProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.homework.yusinsa.common.Constant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesProductUpdateService {

    private final SalesProductRepository salesProductRepository;

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public SalesProductDto updateSalesProduct(final long salesProductId,
                                              final SalesProductSaveDto salesProductSaveDto) {

        throwIfSalesProductSaveRequestIsNotValid(salesProductId, salesProductSaveDto);

        var salesProduct = getSalesProductById(salesProductId);

        salesProduct.updateBrand(
                getBrandById(salesProductSaveDto.getBrand().getId())
        );
        salesProduct.updateCategory(
                getCategoryById(salesProductSaveDto.getCategory().getId())
        );

        salesProduct.updatePrice(
                salesProductSaveDto.getPrice()
        );

        return SalesProductDtoMapper.createSalesProductDtoFrom(salesProduct);
    }

    private void throwIfSalesProductSaveRequestIsNotValid(final long salesProductId,
                                                          final SalesProductSaveDto salesProductSaveRequestDto) throws ApiException {

        var salesProductOfRequestedBrandAndCategory = salesProductRepository.findFirstByBrand_IdAndCategory_Id(
                salesProductSaveRequestDto.getBrand().getId(),
                salesProductSaveRequestDto.getCategory().getId());

        salesProductOfRequestedBrandAndCategory.ifPresent(
                salesProduct -> {
                    if (salesProductId != salesProduct.getId()) {
                        throw ApiException.makeConflict(
                                String.format(SALES_PRODUCT_FORMAT_ALREADY_EXISTS,
                                        salesProductSaveRequestDto.getBrand().getId(),
                                        salesProductSaveRequestDto.getCategory().getId())
                        );
                    }
                }
        );
    }

    private SalesProduct getSalesProductById(final long salesProductId) {
        return salesProductRepository.findById(salesProductId)
                .orElseThrow(() -> ApiException.makeNotFound(SALES_PRODUCT_NOT_FOUND));
    }

    private Brand getBrandById(final long brandId) throws ApiException {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> ApiException.makeNotFound(BRAND_NOT_FOUND));
    }

    private Category getCategoryById(final long categoryId) throws ApiException {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> ApiException.makeNotFound(CATEGORY_NOT_FOUND));
    }

}