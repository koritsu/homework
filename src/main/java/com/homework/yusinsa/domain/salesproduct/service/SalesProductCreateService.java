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

import static com.homework.yusinsa.common.Constant.BRAND_NOT_FOUND;
import static com.homework.yusinsa.common.Constant.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesProductCreateService {

    private final SalesProductRepository salesProductRepository;

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public SalesProductDto createSalesProduct(final SalesProductSaveDto salesProductSaveDto) {

        if (salesProductRepository.existsSalesProductByBrand_IdAndCategory_Id(
                salesProductSaveDto.getBrand().getId(),
                salesProductSaveDto.getCategory().getId())) {
            var errorMessage = "SalesProduct already exists. brandId: " +
                    salesProductSaveDto.getBrand().getId() +
                    ", categoryId: " + salesProductSaveDto.getCategory().getId();

            log.error(errorMessage);
            throw ApiException.makeConflict(errorMessage);
        }

        return SalesProductDtoMapper.createSalesProductDtoFrom(
                salesProductRepository.save(
                        SalesProduct.builder()
                                .brand(
                                        getBrandById(salesProductSaveDto.getBrand().getId())
                                )
                                .category(
                                        getCategoryById(salesProductSaveDto.getCategory().getId())
                                )
                                .price(salesProductSaveDto.getPrice())
                                .build())
        );
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