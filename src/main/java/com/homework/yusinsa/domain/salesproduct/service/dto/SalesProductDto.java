package com.homework.yusinsa.domain.salesproduct.service.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesProductDto {

    private Long id;

    private BrandDto brand;

    private CategoryDto category;

    private Integer price;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BrandDto {

        private Long id;

        private String name;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryDto {

        private Long id;

        private String name;

    }

}

