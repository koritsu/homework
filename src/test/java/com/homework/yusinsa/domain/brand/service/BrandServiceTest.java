package com.homework.yusinsa.domain.brand.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.brand.service.dto.BrandDto;
import com.homework.yusinsa.domain.brand.service.dto.BrandSaveDto;
import com.homework.yusinsa.repository.brand.BrandRepository;
import com.homework.yusinsa.repository.brand.entity.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.homework.yusinsa.common.Constant.BRAND_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        brandService = new BrandService(brandRepository);
    }

    @Test
    @Transactional
    void saveBrand() {
        BrandSaveDto brandSaveDto = BrandSaveDto.builder()
                .name("New Brand")
                .build();

        Brand savedBrand = Brand.builder().id(1L).name("New Brand").build();

        when(brandRepository.save(any(Brand.class))).thenReturn(savedBrand);

        BrandDto result = brandService.saveBrand(brandSaveDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Brand", result.getName());
    }

    @Test
    void getBrands() {
        Brand brand1 = Brand.builder().id(1L).name("Brand 1").build();
        Brand brand2 = Brand.builder().id(2L).name("Brand 2").build();

        when(brandRepository.findAllByOrderById()).thenReturn(List.of(brand1, brand2));

        List<BrandDto> result = brandService.getBrands();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Brand 1", result.get(0).getName());
        assertEquals("Brand 2", result.get(1).getName());
    }

    @Test
    void getBrandById() {
        Brand brand = Brand.builder().id(1L).name("Brand").build();

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        BrandDto result = brandService.getBrandById(1L);

        assertNotNull(result);
        assertEquals("Brand", result.getName());
    }

    @Test
    void getBrandById_NotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class,
                () -> brandService.getBrandById(1L));

        assertEquals(BRAND_NOT_FOUND, exception.getMessage());
    }

    @Test
    @Transactional
    void updateBrand() {
        Brand brand = Brand.builder().id(1L).name("Old Name").build();
        BrandSaveDto brandSaveDto = BrandSaveDto.builder()
                .name("Updated Name")
                .build();

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        BrandDto result = brandService.updateBrand(1L, brandSaveDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void updateBrand_NotFound() {
        BrandSaveDto brandSaveDto = BrandSaveDto.builder()
                .name("Updated Name")
                .build();

        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class,
                () -> brandService.updateBrand(1L, brandSaveDto));

        assertEquals(BRAND_NOT_FOUND, exception.getMessage());
    }

    @Test
    @Transactional
    void deleteBrand() {
        Brand brand = Brand.builder().id(1L).name("Brand").build();

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        brandService.deleteBrand(1L);
    }

    @Test
    void deleteBrand_NotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class,
                () -> brandService.deleteBrand(1L));

        assertEquals(BRAND_NOT_FOUND, exception.getMessage());
    }

}
