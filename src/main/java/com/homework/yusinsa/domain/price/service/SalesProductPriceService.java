package com.homework.yusinsa.domain.price.service;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.domain.price.service.dto.CategoryPricesDto;
import com.homework.yusinsa.domain.price.service.dto.SalesProductPriceDetailDto;
import com.homework.yusinsa.repository.category.CategoryRepository;
import com.homework.yusinsa.repository.category.entity.Category;
import com.homework.yusinsa.repository.salesproduct.SalesProductRepository;
import com.homework.yusinsa.repository.salesproduct.entity.SalesProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.homework.yusinsa.common.Constant.CATEGORY_NOT_FOUND;
import static com.homework.yusinsa.common.Constant.SALES_PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SalesProductPriceService {

    private final CategoryRepository categoryRepository;

    private final SalesProductRepository salesProductRepository;


    /**
     * 카테고리별로 가장 싼 브랜드와 가격을 조회합니다.
     *
     * @return 카테고리별로 가장 싼 브랜드와 가격 목록과 그 합
     */
    public CategoryPricesDto getCheapestBrandAndPricesOfEachCategory() {

        var categoriesByOrderById = categoryRepository.findCategoriesByOrderById();

        if (categoriesByOrderById.isEmpty()) {
            throw ApiException.makeNotFound(SALES_PRODUCT_NOT_FOUND);
        }

        return PriceDtoMapper.createCategoryPricesDtoFrom(
                categoriesByOrderById.stream().map(Category::getLowestPriceSalesProduct).toList());
    }

    /**
     * 카테고리 이름으로 카테고리의 가격 상세를 조회합니다.
     *
     * @param categoryName 카테고리 이름
     * @return 카테고리의 가격 상세
     */
    public SalesProductPriceDetailDto getPriceDetailByCategoryName(final String categoryName) {

        var category = categoryRepository.findFirstByName(categoryName)
                .orElseThrow(() -> ApiException.makeNotFound(CATEGORY_NOT_FOUND));

        var salesProductsByCategoryOrderByPriceMap = salesProductRepository.findSalesProductsByCategoryOrderByPriceAsMap(category);

        if (salesProductsByCategoryOrderByPriceMap.isEmpty()) {
            throw ApiException.makeNotFound(CATEGORY_NOT_FOUND);
        }

        return SalesProductPriceDetailDto.create(
                category.getName(),
                PriceDtoMapper.createSalesProductPriceDetailDtoBrandPriceDtoListFrom(
                        salesProductsByCategoryOrderByPriceMap.firstEntry().getValue()),
                PriceDtoMapper.createSalesProductPriceDetailDtoBrandPriceDtoListFrom(
                        salesProductsByCategoryOrderByPriceMap.lastEntry().getValue())
        );

    }

    /**
     * SalesProductPriceService - PriceDtoMapper
     */

    private static class PriceDtoMapper {

        public static CategoryPricesDto createCategoryPricesDtoFrom(final List<SalesProduct> salesProducts) {
            return CategoryPricesDto.builder()
                    .categories(salesProducts.stream().map(
                            salesProduct ->
                                    CategoryPricesDto.CategoryPriceDto.builder()
                                            .price(salesProduct.getPrice())
                                            .categoryName(salesProduct.getCategory().getName())
                                            .brandName(salesProduct.getBrand().getName())
                                            .build()

                    ).toList())
                    .totalPrice(salesProducts.stream().mapToInt(SalesProduct::getPrice).sum())
                    .build();
        }


        public static List<SalesProductPriceDetailDto.BrandPriceDto> createSalesProductPriceDetailDtoBrandPriceDtoListFrom(
                final List<SalesProduct> salesProducts) {
            return salesProducts.stream().map(
                    salesProduct ->
                            SalesProductPriceDetailDto.BrandPriceDto.builder()
                                    .brandName(salesProduct.getBrand().getName())
                                    .price(salesProduct.getPrice())
                                    .build()
            ).toList();
        }

    }


}
