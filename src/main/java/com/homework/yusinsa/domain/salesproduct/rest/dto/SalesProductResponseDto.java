package com.homework.yusinsa.domain.salesproduct.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SalesProductResponseDto {

    private Long id;

    private BrandResponseDto brand;

    private CategoryResponseDto category;

    private Integer price;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class BrandResponseDto {

        private Long id;

        private String name;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class CategoryResponseDto {

        private Long id;

        private String name;

    }

}