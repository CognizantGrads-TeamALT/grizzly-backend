package com.grizzly.usermicro.customer;

import com.grizzly.usermicro.user.User;

import javax.persistence.*;

@Entity(name="customer")
@Table(name = "customer")
public class Customer extends User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")      private Integer userId;

    @AttributeOverrides({
            @AttributeOverride(name="name", column=@Column(name="name")),
            @AttributeOverride(name="contact_num", column=@Column(name="contact_num")),
            @AttributeOverride(name="email", column=@Column(name="email"))
    })
    @Column(name="address")      private String address;


    public Customer() {
        super();
    }

    public Customer(String name, String contact_num, String email, String address) {
        super(name, contact_num, email);

        setAddress(address);
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
