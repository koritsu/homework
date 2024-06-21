package com.homework.yusinsa.repository.category.entity;

import com.homework.yusinsa.common.ApiException;
import com.homework.yusinsa.repository.salesproduct.entity.SalesProduct;
import jakarta.persistence.*;
import lombok.*;

import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<SalesProduct> salesProducts;

    public SalesProduct getLowestPriceSalesProduct() {
        return this.salesProducts.stream()
                .min(Comparator.comparingInt(SalesProduct::getPrice))
                .orElseThrow(() -> ApiException.makeNotFound("해당 카테고리에 상품이 존재하지 않습니다."));
    }

    public void updateName(String name) {
        this.name = name;
    }

}
