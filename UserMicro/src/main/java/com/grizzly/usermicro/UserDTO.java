package com.grizzly.usermicro;

public class UserDTO {
    private String userId;
    private String name;
    private String contact_num;
    private String email;
    private UserGroup userGroup;

    public UserDTO() {
    }

    public UserDTO(String userId, String name, String contact_num, String email, UserGroup userGroup) {
        this.userId = userId;
        this.name = name;
        this.contact_num = contact_num;
        this.email = email;
        this.userGroup = userGroup;
    }

    public User toEntity() {
        User user = new User();
        user.setName(name);
        user.setContact_num(contact_num);
        user.setEmail(email);
        user.setUserGroup(userGroup);
        return user;
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