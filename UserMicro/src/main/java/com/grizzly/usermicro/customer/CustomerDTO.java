package com.grizzly.usermicro.customer;

import com.grizzly.usermicro.orders.OrderDTO;

public class CustomerDTO {
    private Integer userId;
    private String address;
    private OrderDTO[] orderDTO;

    public CustomerDTO() {
    }

    public CustomerDTO(Integer userId, String address, OrderDTO[] orderDTO) {
        this.userId = userId;
        this.address = address;
        this.orderDTO = orderDTO;
    }

    public Customer toEntity() {
        Customer customer = new Customer();
        customer.setAddress(address);
        return customer;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrderDTO[] getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO[] orderDTO) {
        this.orderDTO = orderDTO;
    }
}
