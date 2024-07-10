package com.homework.template.domain.brand.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandSaveRequestDto {

    private Long id;

    @NotBlank(message = "Brand name is mandatory")
    @Size(max = 255, message = "Brand name must be less than or equal to 255 characters")
    private String name;

}
