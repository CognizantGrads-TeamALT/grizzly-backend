package com.example.demo.VendorMicro;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends CrudRepository<Vendor, String>{
    public Iterable<Vendor> findAllByVendorId(String vendorId);

    public Iterable<Vendor> findAll();
}
