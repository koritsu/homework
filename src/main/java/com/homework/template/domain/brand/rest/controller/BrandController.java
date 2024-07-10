package com.homework.template.domain.brand.rest.controller;

import com.homework.template.domain.brand.BrandDtoMapper;
import com.homework.template.domain.brand.rest.dto.BrandResponseDto;
import com.homework.template.domain.brand.rest.dto.BrandSaveRequestDto;
import com.homework.template.domain.brand.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BrandController - Brand CRUD API Controller
 *
 * @author khkim
 * @since 6/20/24
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/brands")
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public BrandResponseDto createBrand(@Valid @RequestBody BrandSaveRequestDto brandSaveRequestDto) {
        return BrandDtoMapper.createBrandResponseDtoFrom(
                brandService.saveBrand(
                        BrandDtoMapper.createBrandSaveDtoFrom(brandSaveRequestDto)
                )
        );
    }

    @GetMapping
    public List<BrandResponseDto> getBrands() {
        return brandService.getBrands().stream().map(
                BrandDtoMapper::createBrandResponseDtoFrom).toList();
    }

    @GetMapping("/{brandId}")
    public BrandResponseDto getBrand(@PathVariable Long brandId) {
        return BrandDtoMapper.createBrandResponseDtoFrom(
                brandService.getBrandById(brandId)
        );
    }

    @PutMapping("/{brandId}")
    public BrandResponseDto updateProduct(@PathVariable Long brandId, @Valid @RequestBody BrandSaveRequestDto brandSaveRequestDto) {
        return BrandDtoMapper.createBrandResponseDtoFrom(
                brandService.updateBrand(brandId,
                        BrandDtoMapper.createBrandSaveDtoFrom(brandSaveRequestDto))
        );
    }

    @DeleteMapping("/{brandId}")
    public void deleteProduct(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
    }

}
