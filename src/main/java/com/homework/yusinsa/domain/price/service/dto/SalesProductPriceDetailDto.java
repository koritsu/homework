package com.homework.yusinsa.domain.price.service.dto;


import lombok.*;

import java.util.List;

@Getter
@Builder
public class SalesProductPriceDetailDto {

    private String categoryName;

    private List<BrandPriceDto> cheapestPriceBrands;

    private List<BrandPriceDto> mostExpensivePriceBrands;

    private int totalPrice;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BrandPriceDto {

        private String brandName;

        private int price;

    }

    public static SalesProductPriceDetailDto create(final String categoryName,
                                                    final List<BrandPriceDto> cheapestPriceBrands,
                                                    final List<BrandPriceDto> mostExpensivePriceBrands) {

        return SalesProductPriceDetailDto.builder()
                .categoryName(categoryName)
                .cheapestPriceBrands(cheapestPriceBrands)
                .mostExpensivePriceBrands(mostExpensivePriceBrands)
                .build();
    }

}
