package com.grizzly.usermicro;

import javax.persistence.*;

@Entity(name="user")
public class User {
    // For creating the variables

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")      private String userId;

    @Column(name="name")            private String name;
    @Column(name="contact_num")     private String contact_num;
    @Column(name="email")           private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="user_group")      private UserGroup userGroup;

    public User() {
        super();
    }

    public User(String name, String contact_num, String email, UserGroup userGroup) {
        super();

        setName(name);
        setContact_num(contact_num);
        setEmail(email);
        setUserGroup(userGroup);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
}
