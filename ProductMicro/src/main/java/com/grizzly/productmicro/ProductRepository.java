package com.grizzly.productmicro;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    @Query("SELECT p FROM product p WHERE p.productId = :productId")
    List<Product> findByProductId(@Param("productId") Integer productId, Pageable pageable);

    @Query("SELECT p FROM product p WHERE LOWER(p.name) LIKE LOWER(concat(concat('%',:name), '%'))")
    List<Product> findByProductName(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM product p WHERE p.vendorId = :vendorId")
    List<Product> findByVendorId(@Param("vendorId") Integer vendorId);

    @Query("SELECT p FROM product p WHERE p.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);
}