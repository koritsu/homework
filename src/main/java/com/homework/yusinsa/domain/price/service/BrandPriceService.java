package com.homework.yusinsa.domain.price.service;

import com.homework.yusinsa.domain.price.service.dto.BrandPricesDto;
import com.homework.yusinsa.repository.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandPriceService {

    private final BrandRepository brandRepository;

    /**
     * 브랜드별 카테고리 들의 가격 합이 최저가인 카테고리 조회
     *
     * @return BrandPricesDto
     */
    public BrandPricesDto getCheapestTotalCategoryPricesOfEachBrand() {

        var cheapestBrand = brandRepository.findFirstCheapestBrand();

        return PriceDtoMapper.createBrandPricesDtoFrom(
                cheapestBrand.getName(),
                cheapestBrand.getSalesProducts().stream()
                        .filter(salesProduct -> salesProduct.getCategory() != null)
                        .map(
                        salesProduct ->
                                BrandPricesDto.BrandPriceCategoryDto.builder()
                                        .price(salesProduct.getPrice())
                                        .categoryName(salesProduct.getCategory().getName())
                                        .build()
                ).toList()
        );
    }

    /**
     * 카테고리별 최저가 브랜드 조회
     */
    private static class PriceDtoMapper {

        public static BrandPricesDto createBrandPricesDtoFrom(final String brandName, final List<BrandPricesDto.BrandPriceCategoryDto> brandPriceCategoryDtos) {

            return BrandPricesDto.builder()
                    .brandName(brandName)
                    .categories(brandPriceCategoryDtos)
                    .totalPrice(
                            brandPriceCategoryDtos.stream().mapToInt(BrandPricesDto.BrandPriceCategoryDto::getPrice).sum()
                    )
                    .build();
        }

    }

}
