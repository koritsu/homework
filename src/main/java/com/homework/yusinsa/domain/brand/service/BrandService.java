package com.homework.yusinsa.domain.brand.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.brand.BrandDtoMapper;
import com.homework.yusinsa.domain.brand.service.dto.BrandDto;
import com.homework.yusinsa.domain.brand.service.dto.BrandSaveDto;
import com.homework.yusinsa.repository.brand.BrandRepository;
import com.homework.yusinsa.repository.brand.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.homework.yusinsa.common.Constant.BRAND_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandDto saveBrand(final BrandSaveDto brandSaveDto) {
        return BrandDtoMapper.createBrandDtoFrom(brandRepository.save(
                BrandDtoMapper.createBrandEntityFrom(brandSaveDto)
        ));
    }

    public List<BrandDto> getBrands() {
        return brandRepository.findAllByOrderById().stream()
                .map(BrandDtoMapper::createBrandDtoFrom)
                .toList();
    }

    public BrandDto getBrandById(final long id) {
        return brandRepository.findById(id)
                .map(BrandDtoMapper::createBrandDtoFrom)
                .orElseThrow(
                        () -> ApiException.makeNotFound(BRAND_NOT_FOUND)
                );
    }

    @Transactional
    public BrandDto updateBrand(final long brandId, final BrandSaveDto brandSaveDto) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(
                () -> ApiException.makeNotFound(BRAND_NOT_FOUND)
        );

        brand.updateName(brandSaveDto.getName());
        return BrandDtoMapper.createBrandDtoFrom(brand);
    }

    public void deleteBrand(final long brandId) {
        brandRepository.findById(brandId).ifPresentOrElse(
                brandRepository::delete,
                () -> {
                    throw ApiException.makeNotFound(BRAND_NOT_FOUND);
                }
        );
    }

}