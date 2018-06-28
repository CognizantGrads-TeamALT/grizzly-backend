package com.grizzly.usermicro.vendor;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor, Integer> {
    @Query("SELECT u FROM vendor u WHERE u.userId = :userId")
    List<Vendor> findByUserVendorId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT u FROM vendor u WHERE u.email = :email")
    Vendor findByUserEmail(@Param("email") String email);
}
