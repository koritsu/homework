package com.homework.yusinsa.repository.salesproduct.entity;

import com.homework.yusinsa.repository.brand.entity.Brand;
import com.homework.yusinsa.repository.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Category category;

    private int price;

    public void updateBrand(Brand brand) {
        this.brand = brand;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

}
