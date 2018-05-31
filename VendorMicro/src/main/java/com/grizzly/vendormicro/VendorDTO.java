package com.grizzly.vendormicro;

public class VendorDTO {
    private String vendorId;
    private String name;
    private String contactNum;
    private String website;
    private String email;
    private String bio;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Vendor toEntity() {
        Vendor vendor = new Vendor();
        vendor.setVendorId(this.vendorId);
        vendor.setName(this.name);
        vendor.setContactNum(this.contactNum);
        vendor.setWebsite(this.website);
        vendor.setEmail(this.email);
        vendor.setBio(this.bio);
        return vendor;
    }
}
