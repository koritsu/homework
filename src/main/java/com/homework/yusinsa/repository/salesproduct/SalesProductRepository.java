package com.homework.yusinsa.repository.salesproduct;

import com.homework.yusinsa.repository.category.entity.Category;
import com.homework.yusinsa.repository.salesproduct.entity.SalesProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public interface SalesProductRepository extends JpaRepository<SalesProduct, Long> {

    boolean existsSalesProductByBrand_IdAndCategory_Id(final long brandId, final long categoryId);

    Optional<SalesProduct> findFirstByBrand_IdAndCategory_Id(final long brandId, final long categoryId);

    List<SalesProduct> findSalesProductsByCategory(final Category category);

    default TreeMap<Integer, List<SalesProduct>> findSalesProductsByCategoryOrderByPriceAsMap(final Category category) {
        return this.findSalesProductsByCategory(category).stream()
                .sorted(Comparator.comparing(SalesProduct::getPrice))
                .collect(Collectors.groupingBy(
                        SalesProduct::getPrice,
                        //() -> new TreeMap<>(Comparator.comparingInt(Integer::intValue)),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

}
