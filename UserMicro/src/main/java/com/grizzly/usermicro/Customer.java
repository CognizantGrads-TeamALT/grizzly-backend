package com.grizzly.usermicro;

import javax.persistence.*;

@Entity(name="customer")
public class Customer extends User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")      private String userId;

    @Column(name="address")      private String address;


    public Customer() {
        super();
    }

    public Customer(String address) {
        super();

        setAddress(address);
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
