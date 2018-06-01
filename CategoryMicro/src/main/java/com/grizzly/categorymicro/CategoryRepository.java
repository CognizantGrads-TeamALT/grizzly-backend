package com.grizzly.categorymicro;

//import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    @Query("SELECT c FROM category c WHERE c.categoryId = :categoryId")
    List<Category> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);

    @Query("SELECT c FROM category c WHERE LOWER(c.name) LIKE LOWER(concat(concat('%',:name), '%'))")
    List<Category> findByCategoryName(@Param("name") String name, Pageable pageable);
}