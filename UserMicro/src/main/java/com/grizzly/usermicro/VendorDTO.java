package com.grizzly.usermicro;

public class VendorDTO {
    private String userId;
    private String address;
    private String website;
    private String about;
    private String productPortfolio;

    public VendorDTO() {
    }

    public VendorDTO(String address, String website, String about, String productPortfolio) {
        this.address = address;
        this.website = website;
        this.about = about;
        this.productPortfolio = productPortfolio;
    }

    public Vendor toEntity() {
        Vendor vendor = new Vendor();
        vendor.setAddress(address);
        vendor.setWebsite(website);
        vendor.setAbout(about);
        vendor.setProductPortfolio(productPortfolio);
        return vendor;
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
        return productPortfolio;
    }

    public void setProductPortfolio(String productPortfolio) {
        this.productPortfolio = productPortfolio;
    }
}
