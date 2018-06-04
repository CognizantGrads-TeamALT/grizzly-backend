package com.grizzly.vendormicro;

//import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor, Integer> {
    @Query("SELECT v FROM vendor v WHERE v.vendorId = :vendorId")
    List<Vendor> findByVendorId(@Param("vendorId") Integer vendorId, Pageable pageable);

    @Query("SELECT v FROM vendor v WHERE LOWER(v.name) LIKE LOWER(concat(concat('%',:name), '%'))")
    List<Vendor> findByVendorName(@Param("name") String name, Pageable pageable);
}