package com.homework.template.domain.brand;

import com.homework.template.domain.brand.rest.dto.BrandResponseDto;
import com.homework.template.domain.brand.rest.dto.BrandSaveRequestDto;
import com.homework.template.domain.brand.service.dto.BrandDto;
import com.homework.template.domain.brand.service.dto.BrandSaveDto;
import com.homework.template.repository.brand.entity.Brand;

public class BrandDtoMapper {

    public static Brand createBrandEntityFrom(final BrandSaveDto brandSaveDto) {
        return Brand.builder()
                .name(brandSaveDto.getName())
                .build();
    }

    public static BrandDto createBrandDtoFrom(final Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }

    public static BrandResponseDto createBrandResponseDtoFrom(final BrandDto brandDto) {
        return BrandResponseDto.builder()
                .id(brandDto.getId())
                .name(brandDto.getName())
                .build();
    }

    public static BrandSaveDto createBrandSaveDtoFrom(final BrandSaveRequestDto brandSaveRequestDto) {
        return BrandSaveDto.builder()
                .name(brandSaveRequestDto.getName())
                .build();
    }

}
