package com.grizzly.usermicro.vendor;

import com.grizzly.usermicro.user.UserDTO;

public class VendorDTO extends UserDTO {
    private Integer userId;
    private String address;
    private String website;
    private String about;
    private String product_portfolio;

    public VendorDTO() {
        super();
    }

    public VendorDTO(Integer userId, String address, String website, String about, String product_portfolio) {
        this.userId = userId;
        this.address = address;
        this.website = website;
        this.about = about;
        this.product_portfolio = product_portfolio;
    }

    public Vendor toEntity() {
        Vendor vendor = new Vendor();
        vendor.setAddress(address);
        vendor.setWebsite(website);
        vendor.setAbout(about);
        vendor.setProductPortfolio(product_portfolio);
        return vendor;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProductPortfolio() {
        return product_portfolio;
    }

    public void setProductPortfolio(String product_portfolio) {
        this.product_portfolio = product_portfolio;
    }
}
