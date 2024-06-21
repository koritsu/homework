package com.homework.yusinsa.domain.salesproduct.rest.controller;

import com.homework.yusinsa.domain.salesproduct.rest.dto.SalesProductResponseDto;
import com.homework.yusinsa.domain.salesproduct.rest.dto.SalesProductSaveRequestDto;
import com.homework.yusinsa.domain.salesproduct.service.SalesProductCreateService;
import com.homework.yusinsa.domain.salesproduct.service.SalesProductDeleteService;
import com.homework.yusinsa.domain.salesproduct.service.SalesProductReadService;
import com.homework.yusinsa.domain.salesproduct.service.SalesProductUpdateService;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductDto;
import com.homework.yusinsa.domain.salesproduct.service.dto.SalesProductSaveDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * SalesProductController - SalesProduct CRUD API Controller
 *
 * @author khkim
 * @since 6/18/24
 **/
@RestController
@RequestMapping("/v1/sales-products")
@RequiredArgsConstructor
public class SalesProductController {

    private final SalesProductCreateService salesProductCreateService;

    private final SalesProductReadService salesProductReadService;

    private final SalesProductDeleteService salesProductDeleteService;

    private final SalesProductUpdateService salesProductUpdateService;

    @GetMapping("/{salesProductId}")
    public SalesProductResponseDto getSalesProduct(@PathVariable final long salesProductId) {
        return DtoMapper.createSalesProductResponseDtoFrom(
                salesProductReadService.getSalesProductById(salesProductId)
        );
    }

    @GetMapping
    public List<SalesProductResponseDto> getSalesProducts() {
        return salesProductReadService.getSalesProducts().stream()
                .map(DtoMapper::createSalesProductResponseDtoFrom).toList();
    }

    @PostMapping
    public SalesProductResponseDto createSalesProduct(@Valid @RequestBody final SalesProductSaveRequestDto salesProductSaveRequestDto) {
        return DtoMapper.createSalesProductResponseDtoFrom(
                salesProductCreateService.createSalesProduct(
                        DtoMapper.createSalesProductSaveDtoFrom(salesProductSaveRequestDto)
                )
        );
    }

    @PutMapping("/{salesProductId}")
    public SalesProductResponseDto updateSalesProduct(@PathVariable final long salesProductId,
                                                      @Valid @RequestBody final SalesProductSaveRequestDto salesProductSaveRequestDto) {
        return DtoMapper.createSalesProductResponseDtoFrom(
                salesProductUpdateService.updateSalesProduct(salesProductId,
                        DtoMapper.createSalesProductSaveDtoFrom(salesProductSaveRequestDto))
        );
    }

    @DeleteMapping("/{salesProductId}")
    public void deleteSalesProduct(@PathVariable final long salesProductId) {
        salesProductDeleteService.deleteSalesProductById(salesProductId);
    }


    private static class DtoMapper {

        public static SalesProductSaveDto createSalesProductSaveDtoFrom(final SalesProductSaveRequestDto salesProductSaveRequestDto) {
            return SalesProductSaveDto.builder()
                    .brand(SalesProductSaveDto.Brand.builder()
                            .id(salesProductSaveRequestDto.getBrand().getId())
                            .build())
                    .category(SalesProductSaveDto.Category.builder()
                            .id(salesProductSaveRequestDto.getCategory().getId())
                            .build())
                    .price(salesProductSaveRequestDto.getPrice())
                    .build();
        }

        public static SalesProductResponseDto createSalesProductResponseDtoFrom(final SalesProductDto salesProductDto) {
            return SalesProductResponseDto.builder()
                    .id(salesProductDto.getId())
                    .brand(
                            Optional.ofNullable(salesProductDto.getBrand()).map(
                                    brandDto ->
                                            SalesProductResponseDto.BrandResponseDto.builder()
                                                    .id(salesProductDto.getBrand().getId())
                                                    .name(salesProductDto.getBrand().getName())
                                                    .build()
                            ).orElse(null)
                    )
                    .category(
                            Optional.ofNullable(salesProductDto.getCategory()).map(
                                    categoryDto ->
                                            SalesProductResponseDto.CategoryResponseDto.builder()
                                                    .id(salesProductDto.getCategory().getId())
                                                    .name(salesProductDto.getCategory().getName())
                                                    .build()
                            ).orElse(null)
                    )
                    .price(salesProductDto.getPrice())
                    .build();
        }

    }

}