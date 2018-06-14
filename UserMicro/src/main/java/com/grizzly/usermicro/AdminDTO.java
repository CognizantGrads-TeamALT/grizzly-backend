package com.grizzly.usermicro;

public class AdminDTO {
    private String userId;
    private String job_position;
    private String office;

    public AdminDTO() {
    }

    public AdminDTO(String userId, String job_position, String office) {
        this.job_position = job_position;
        this.office = office;
    }

    public Admin toEntity() {
        Admin admin = new Admin();
        admin.setJobPosition(job_position);
        admin.setOffice(office);
        return admin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

