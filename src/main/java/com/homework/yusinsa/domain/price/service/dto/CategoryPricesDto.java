package com.homework.yusinsa.domain.price.service.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryPricesDto {

    private List<CategoryPriceDto> categories;

    private int totalPrice;

    @Builder
    @Getter
    public static class CategoryPriceDto {

        private String categoryName;

        private String brandName;

        private int price;

    }

}
