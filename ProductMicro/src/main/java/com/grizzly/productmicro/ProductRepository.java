package com.grizzly.productmicro;

//import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, String> {
    @Query("SELECT p FROM product p WHERE p.productId = :productId OR p.name = :name")
    List<Product> findByProductIdOrName(@Param("productId") String productId, @Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM product p WHERE p.productId = :productId")
    List<Product> findByProductId(@Param("productId") String productId, Pageable pageable);
}