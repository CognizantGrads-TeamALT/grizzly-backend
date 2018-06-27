package com.grizzly.usermicro.customer;

public class CustomerDTO {
    private Integer userId;
    private String address;

    public CustomerDTO(Integer userId, String address) {
        this.userId = userId;
        this.address = address;
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
}
