package com.grizzly.usermicro.customer;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {
    @Query("SELECT u FROM customer u WHERE u.userId = :userId")
    List<Customer> findByUserCustomerId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT u FROM customer u WHERE u.email = :email")
    Customer findByUserEmail(@Param("email") String email);
}