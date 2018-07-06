package com.grizzly.productmicro.image;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends PagingAndSortingRepository<Image, Integer> {
    @Query("SELECT i FROM product_image i WHERE i.product_id = :product_id")
    List<Image> findByProductId(@Param("product_id") Integer product_id);
}
