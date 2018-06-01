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
public interface CategoryRepository extends PagingAndSortingRepository<Category, String> {
    @Query("SELECT c FROM category c WHERE c.categoryId = :categoryId OR c.name = :name")
    List<Vendor> findByCategoryIdOrName(@Param("categoryId") String categoryId, @Param("name") String name, Pageable pageable);

    @Query("SELECT c FROM category c WHERE c.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);
}