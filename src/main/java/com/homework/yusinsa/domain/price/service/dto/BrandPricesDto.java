package com.homework.yusinsa.domain.price.service.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BrandPricesDto {

    private String brandName;

    private List<BrandPriceCategoryDto> categories;

    private int totalPrice;

    @Builder
    @Getter
    public static class BrandPriceCategoryDto {

        private String categoryName;

        private int price;

    }

}
