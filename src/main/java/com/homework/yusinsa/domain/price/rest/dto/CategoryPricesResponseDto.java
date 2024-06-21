package com.homework.yusinsa.domain.price.rest.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoryPricesResponseDto {

    private List<CategoryResponseDto> categories;

    private int totalPrice;

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class CategoryResponseDto {

        private String categoryName;

        private String brandName;

        private int price;

    }

}
