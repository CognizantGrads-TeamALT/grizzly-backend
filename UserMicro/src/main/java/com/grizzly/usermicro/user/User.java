package com.grizzly.usermicro.user;


import javax.persistence.*;

@MappedSuperclass
public class User {
    // For creating the variables

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")         private Integer userId;

    @Column(name="name")            private String name;
    @Column(name="contact_num")     private String contact_num;
    @Column(name="email")           private String email;


    public User() {
        super();
    }

    public User(String name, String contact_num, String email) {
        super();

        setName(name);
        setContact_num(contact_num);
        setEmail(email);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_num() {
        return contact_num;
    }

    public void setContact_num(String contact_num) {
        this.contact_num = contact_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
