package com.example.demo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor, String>{
    Iterable<Vendor> findByVendorIdOrName(String vendorId, String name);
}
