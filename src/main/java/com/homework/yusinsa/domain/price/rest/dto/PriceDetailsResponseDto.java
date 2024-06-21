package com.homework.yusinsa.domain.price.rest.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PriceDetailsResponseDto {

    private String categoryName;

    private List<BrandPriceDto> cheapestPriceBrands;

    private List<BrandPriceDto> mostExpensivePriceBrands;

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class BrandPriceDto {

        private String brandName;

        private int price;

    }

}
