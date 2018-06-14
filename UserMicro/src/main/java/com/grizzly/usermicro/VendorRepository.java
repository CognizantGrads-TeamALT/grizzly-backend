package com.grizzly.usermicro;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor, String> {
    @Query("SELECT v FROM vendor v WHERE v.userId = :userId")
    List<Vendor> findByUserVendorId(@Param("userId") String userId, Pageable pageable);
}
