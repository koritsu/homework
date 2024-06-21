package com.homework.yusinsa.repository.category;

import com.homework.yusinsa.repository.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c from Category c JOIN FETCH c.salesProducts sp JOIN FETCH sp.brand ORDER BY c.id")
    List<Category> findCategoriesByOrderById();

    Optional<Category> findFirstByName(final String categoryName);

    List<Category> findAllByOrderById();

}
