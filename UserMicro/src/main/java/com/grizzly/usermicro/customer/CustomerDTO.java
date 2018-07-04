package com.grizzly.usermicro.customer;

import com.grizzly.usermicro.orderitem.OrderItemDTO;
import com.grizzly.usermicro.orders.OrderDTO;
import com.grizzly.usermicro.user.UserDTO;

import java.util.ArrayList;

public class CustomerDTO extends UserDTO {
    private Integer userId;
    private String address;
    private ArrayList<OrderDTO> orderDTO;

    public CustomerDTO() {
        super();
    }

    public CustomerDTO(Integer userId, String address, ArrayList<OrderDTO> orderDTO) {
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

    public ArrayList<OrderDTO> getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(ArrayList<OrderDTO> orderDTO) {
        this.orderDTO = orderDTO;
    }
}
