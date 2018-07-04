package com.grizzly.usermicro.orderitem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends PagingAndSortingRepository<OrderItem, Integer> {

    @Query("SELECT i FROM order_item i WHERE i.order_id = :order_id")
    List<OrderItem> findItemsByOrderId(@Param("order_id") Integer order_id);

}
