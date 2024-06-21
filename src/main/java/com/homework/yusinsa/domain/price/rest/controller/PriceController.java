package com.homework.yusinsa.domain.price.rest.controller;

import com.homework.yusinsa.domain.price.rest.dto.BrandPricesResponseDto;
import com.homework.yusinsa.domain.price.rest.dto.CategoryPricesResponseDto;
import com.homework.yusinsa.domain.price.rest.dto.PriceDetailsResponseDto;
import com.homework.yusinsa.domain.price.service.BrandPriceService;
import com.homework.yusinsa.domain.price.service.SalesProductPriceService;
import com.homework.yusinsa.domain.price.service.dto.BrandPricesDto;
import com.homework.yusinsa.domain.price.service.dto.CategoryPricesDto;
import com.homework.yusinsa.domain.price.service.dto.SalesProductPriceDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * PriceController - SalesProduct Cheapest Prices API Controller
 *
 * @author khkim
 * @since 6/18/24
 **/
@RestController
@RequestMapping("/v1/prices")
@RequiredArgsConstructor
public class PriceController {

    private final BrandPriceService brandPriceService;

    private final SalesProductPriceService salesProductPriceService;

    /**
     * 브랜드별 카테고리 들의 가격 합이 최저가인 조회 API
     *
     * @return BrandPricesResponseDto
     */
    @GetMapping("/brands/cheapest-total-category-price")
    public BrandPricesResponseDto getCheapestTotalCategoryPricesOfEachBrand() {
        return PriceDtoMapper.createBrandPricesResponseDtoFrom(
                brandPriceService.getCheapestTotalCategoryPricesOfEachBrand()
        );
    }

    /**
     * 카테고리별 최저가 브랜드 조회 API
     *
     * @return CategoryPricesResponseDto
     */

    @GetMapping("/categories/cheapest-brand")
    public CategoryPricesResponseDto getCheapestBrandAndPricesOfEachCategory() {
        return PriceDtoMapper.createCategoryPricesResponseFrom(
                salesProductPriceService.getCheapestBrandAndPricesOfEachCategory()
        );
    }

    /**
     * 카테고리별 가격 상세 조회 API
     *
     * @param categoryName 카테고리 이름
     * @return PriceDetailsResponseDto
     */
    @GetMapping("/detail/categories/{categoryName}")
    public PriceDetailsResponseDto getPriceDetailByCategoryName(@PathVariable final String categoryName) {
        return PriceDtoMapper.createPriceDetailsResponseDtoFrom(
                salesProductPriceService.getPriceDetailByCategoryName(categoryName)
        );
    }


    /**
     * PriceDtoMapper
     */
    private static class PriceDtoMapper {

        public static CategoryPricesResponseDto createCategoryPricesResponseFrom(final CategoryPricesDto categoryPricesDto) {

            return CategoryPricesResponseDto.builder()
                    .categories(
                            categoryPricesDto.getCategories().stream()
                                    .map(
                                            categoryPriceDto ->
                                                    CategoryPricesResponseDto.CategoryResponseDto.builder()
                                                            .categoryName(categoryPriceDto.getCategoryName())
                                                            .brandName(categoryPriceDto.getBrandName())
                                                            .price(categoryPriceDto.getPrice())
                                                            .build()
                                    )
                                    .toList()
                    )
                    .totalPrice(
                            categoryPricesDto.getTotalPrice()
                    )
                    .build();
        }

        public static List<PriceDetailsResponseDto.BrandPriceDto> createPriceDetailsResponseBrandPriceDtoListFrom(
                final List<SalesProductPriceDetailDto.BrandPriceDto> brandPriceDtos) {
            return brandPriceDtos.stream().map(
                    brandPriceDto ->
                            PriceDetailsResponseDto.BrandPriceDto.builder()
                                    .brandName(brandPriceDto.getBrandName())
                                    .price(brandPriceDto.getPrice())
                                    .build()
            ).toList();
        }


        public static PriceDetailsResponseDto createPriceDetailsResponseDtoFrom(final SalesProductPriceDetailDto salesProductPriceDetailDto) {
            return PriceDetailsResponseDto.builder()
                    .categoryName(salesProductPriceDetailDto.getCategoryName())
                    .cheapestPriceBrands(
                            PriceDtoMapper.createPriceDetailsResponseBrandPriceDtoListFrom(salesProductPriceDetailDto.getCheapestPriceBrands())
                    )
                    .mostExpensivePriceBrands(
                            PriceDtoMapper.createPriceDetailsResponseBrandPriceDtoListFrom(salesProductPriceDetailDto.getMostExpensivePriceBrands())
                    )
                    .build();
        }

        public static BrandPricesResponseDto createBrandPricesResponseDtoFrom(final BrandPricesDto brandPricesDto) {

            return BrandPricesResponseDto.builder()
                    .brandName(brandPricesDto.getBrandName())
                    .categories(
                            brandPricesDto.getCategories().stream()
                                    .map(
                                            brandPriceCategoryDto ->
                                                    BrandPricesResponseDto.CategoryResponseDto.builder()
                                                            .categoryName(brandPriceCategoryDto.getCategoryName())
                                                            .price(brandPriceCategoryDto.getPrice())
                                                            .build()
                                    )
                                    .toList()
                    )
                    .totalPrice(
                            brandPricesDto.getTotalPrice()
                    )
                    .build();

        }

    }

}