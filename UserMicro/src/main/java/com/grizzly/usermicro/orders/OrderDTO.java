package com.grizzly.usermicro.orders;

import com.grizzly.usermicro.customer.Customer;
import com.grizzly.usermicro.orderitem.OrderItemDTO;

import java.time.LocalDate;

public class OrderDTO {
    private Integer user_id;
    private Integer txn_id;
    private Double cost;
    private String destination;
    private java.time.LocalDate shipped_on;
    private OrderItemDTO[] orderItemDTO;

    public OrderDTO() {
    }

    public OrderDTO(Integer user_id, Integer txn_id, Double cost, String destination, LocalDate shipped_on, OrderItemDTO[] orderItemDTO) {
        this.user_id = user_id;
        this.txn_id = txn_id;
        this.cost = cost;
        this.destination = destination;
        this.shipped_on = shipped_on;
        this.orderItemDTO = orderItemDTO;
    }

    public Order toEntity() {
        Order order = new Order();
        order.setUser_id(user_id);
        order.setTxn_id(txn_id);
        order.setCost(cost);
        order.setDestination(destination);
        order.setShipped_on(shipped_on);
        return order;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(Integer txn_id) {
        this.txn_id = txn_id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getShipped_on() {
        return shipped_on;
    }

    public void setShipped_on(LocalDate shipped_on) {
        this.shipped_on = shipped_on;
    }

    public OrderItemDTO[] getOrderItemDTO() {
        return orderItemDTO;
    }

    public void setOrderItemDTO(OrderItemDTO[] orderItemDTO) {
        this.orderItemDTO = orderItemDTO;
    }
}
