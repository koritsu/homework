package com.homework.yusinsa.domain.category.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategorySaveRequestDto {

    @NotBlank(message = "Category name is mandatory")
    @Size(max = 255, message = "Category name must be less than or equal to 255 characters")
    private String name;

}