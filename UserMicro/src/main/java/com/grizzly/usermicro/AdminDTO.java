package com.grizzly.usermicro;

public class AdminDTO {
    private String userId;
    private String position;
    private String office;

    public AdminDTO() {
    }

    public AdminDTO(String userId, String position, String office) {
        this.userId = userId;
        this.position = position;
        this.office = office;
    }

    public Admin toEntity() {
        Admin admin = new Admin();
        admin.setUserId(userId);
        admin.setPosition(position);
        admin.setOffice(office);
        return admin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}

