package com.homework.yusinsa.repository.brand;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.repository.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Comparator;
import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findAllByOrderById();

    default Brand findFirstCheapestBrand() {

        return this.findAll().stream().min(Comparator.comparingInt(
                Brand::getSumOfSalesProductPrices
        )).stream().findFirst().orElseThrow(() -> ApiException.makeNotFound("No brand found with the lowest total product price"));
    }


}
