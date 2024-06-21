package com.homework.yusinsa.repository.brand.entity;

import com.homework.yusinsa.repository.salesproduct.entity.SalesProduct;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "brand")
    private List<SalesProduct> salesProducts;

    public int getSumOfSalesProductPrices() {
        return salesProducts.stream()
                .mapToInt(SalesProduct::getPrice)
                .sum();
    }

    public void updateName(String name) {
        this.name = name;
    }

}
