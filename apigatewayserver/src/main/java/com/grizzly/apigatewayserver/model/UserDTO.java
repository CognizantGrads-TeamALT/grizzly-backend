package com.grizzly.apigatewayserver.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class UserDTO {
    private Integer userId;
    private String name;
    private String contact_num;
    private String email;

    public UserDTO() {}

    public UserDTO(String name, String contact_num, String email) {
        this.name = name;
        this.contact_num = contact_num;
        this.email = email;
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