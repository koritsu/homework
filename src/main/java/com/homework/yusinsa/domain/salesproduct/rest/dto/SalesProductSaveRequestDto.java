package com.homework.yusinsa.domain.salesproduct.rest.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesProductSaveRequestDto {

    @NotNull(message = "Brand is mandatory")
    private Brand brand;

    @NotNull(message = "Category is mandatory")
    private Category category;

    @NotNull(message = "Price is mandatory")
    @Digits(integer = 10, fraction = 0, message = "Price must be a number")
    private Integer price;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Brand {

        @NotNull(message = "Brand ID is mandatory")
        @Digits(integer = 19, fraction = 0, message = "Brand ID must be a number")
        private Long id;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Category {

        @NotNull(message = "Category ID is mandatory")
        @Digits(integer = 19, fraction = 0, message = "Category ID must be a number")
        private Long id;

    }

}