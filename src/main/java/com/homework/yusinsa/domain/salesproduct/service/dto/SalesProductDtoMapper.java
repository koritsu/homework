package com.homework.yusinsa.domain.salesproduct.service.dto;

import com.homework.yusinsa.repository.salesproduct.entity.SalesProduct;

import java.util.Optional;

public class SalesProductDtoMapper {

    public static SalesProductDto createSalesProductDtoFrom(final SalesProduct salesProduct) {
        return SalesProductDto.builder()
                .id(salesProduct.getId())
                .brand(
                        Optional.ofNullable(salesProduct.getBrand()).map(
                                brand -> SalesProductDto.BrandDto.builder()
                                        .id(brand.getId())
                                        .name(brand.getName())
                                        .build()
                        ).orElse(null)
                )
                .category(
                        Optional.ofNullable(salesProduct.getCategory()).map(
                                category ->
                                        SalesProductDto.CategoryDto.builder()
                                                .id(category.getId())
                                                .name(category.getName())
                                                .build()
                        ).orElse(null)
                )
                .price(salesProduct.getPrice())
                .build();
    }


}
