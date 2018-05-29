package com.grizzly.vendormicro;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor, String> {

    @Query("SELECT v FROM vendor v WHERE v.vendorId = :vendorId OR v.name = :name")
    List<Vendor> findByVendorIdOrName(@Param("vendorId") String vendorId, @Param("name") String name, Pageable pageable);
}
