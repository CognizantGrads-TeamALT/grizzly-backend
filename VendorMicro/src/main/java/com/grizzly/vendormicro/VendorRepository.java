package com.grizzly.vendormicro;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor, String> {

    @Query("SELECT v from vendor v where v.vendorId = ?1 or v.name = ?2 ORDER BY v.vendorId ASC")
    Iterable<Vendor> findByVendorIdOrName(String vendorId, String name);
}
