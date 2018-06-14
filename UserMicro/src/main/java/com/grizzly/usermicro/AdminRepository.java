package com.grizzly.usermicro;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends PagingAndSortingRepository<Admin, String> {
    @Query("SELECT u FROM admin u WHERE u.userId = :userId")
    List<Admin> findByUserAdminId(@Param("userId") String userId, Pageable pageable);
}
