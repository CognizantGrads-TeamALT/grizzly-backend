package com.grizzly.productmicro;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    @Query("SELECT p FROM product p WHERE p.productId = :productId")
    List<Product> findByProductId(@Param("productId") Integer productId, Pageable pageable);

    @Query("SELECT p FROM product p WHERE LOWER(p.name) LIKE LOWER(concat(concat('%',:name), '%'))")
    List<Product> findByProductName(@Param("name") String name, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE product p SET enabled = false, vendorId = 0 WHERE p.vendorId = :vendorId")
    void disableByVendorId(@Param("vendorId") Integer vendorId);

    @Transactional
    @Modifying
    @Query("UPDATE product p SET enabled = false, categoryId = 0 WHERE p.categoryId = :categoryId")
    void disableByCategoryId(@Param("categoryId") Integer categoryId);

    @Query("SELECT p FROM product p WHERE p.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);
}