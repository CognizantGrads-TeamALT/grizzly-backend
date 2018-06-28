package com.grizzly.usermicro.admin;

import com.grizzly.usermicro.user.UserDTO;

public class AdminDTO extends UserDTO {
    private Integer userId;
    private String job_position;
    private String office;

    public AdminDTO() {
        super();
    }

    public AdminDTO(Integer userId, String job_position, String office) {
        this.userId = userId;
        this.job_position = job_position;
        this.office = office;
    }

    public Admin toEntity() {
        Admin admin = new Admin();
        admin.setJobPosition(job_position);
        admin.setOffice(office);
        return admin;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getJobPosition() {
        return job_position;
    }

    public void setJobPosition(String job_position) {
        this.job_position = job_position;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}

