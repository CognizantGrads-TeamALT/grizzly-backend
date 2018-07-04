package com.grizzly.usermicro.orders;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    @Query("SELECT i FROM user_order i WHERE i.user_id = :user_id")
    List<Order> findByUserId(@Param("user_id") Integer user_id);

}
