package com.grizzly.usermicro;

public class CustomerDTO {
    private String userId;
    private String address;

    public CustomerDTO() {
    }

    public CustomerDTO(String userId, String address) {
        this.userId = userId;
        this.address = address;
    }

    public Customer toEntity() {
        Customer customer = new Customer();
        customer.setAddress(address);
        return customer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
