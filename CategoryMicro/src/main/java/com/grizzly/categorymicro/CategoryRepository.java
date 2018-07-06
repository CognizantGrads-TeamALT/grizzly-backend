package com.grizzly.categorymicro;

import com.grizzly.categorymicro.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    @Query("SELECT c FROM category c WHERE c.categoryId = :categoryId")
    List<Category> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);

    @Query("SELECT c FROM category c WHERE LOWER(c.name) LIKE LOWER(concat(concat('%',:name), '%'))")
    List<Category> findByCategoryName(@Param("name") String name, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE category c SET c.productCount = :newCount WHERE c.categoryId = :categoryId")
    void updateCategoryCount(@Param("categoryId") Integer categoryId, @Param("newCount") Integer newCount);

    List<Category> findByCategoryIdIn(List<Integer> catIds);
}