package com.grizzly.vendormicro.image;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends PagingAndSortingRepository<Image, Integer> {
    @Query("SELECT i FROM vendor_image i WHERE i.vendor_id = :vendor_id")
    List<Image> findByVendorId(@Param("vendor_id") Integer vendor_id);

    @Query("SELECT i FROM vendor_image i WHERE i.vendor_id = :vendor_id AND i.image_url = :image_url")
    Image findByVendorIdAndName(@Param("vendor_id") Integer vendor_id, @Param("image_url") String image_url);
}
