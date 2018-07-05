package com.grizzly.vendormicro.model;

import com.grizzly.vendormicro.image.ImageDTO;

public class VendorDTO {
    private Integer vendorId;
    private String name;
    private String contactNum;
    private String website;
    private String email;
    private String bio;
    private Boolean enabled;
    private ImageDTO[] imageDTO;

    public VendorDTO(String name, String contactNum, String website, String email, String bio, Boolean enabled, ImageDTO[] imageDTO) {
        super();

        this.name = name;
        this.contactNum = contactNum;
        this.website = website;
        this.email = email;
        this.bio = bio;
        this.enabled = enabled;
        this.imageDTO = imageDTO;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Vendor toEntity() {
        Vendor vendor = new Vendor(this.name, this.contactNum, this.website, this.email, this.bio, this.enabled);
        return vendor;
    }

    public ImageDTO[] getImageDTO() {
        return imageDTO;
    }

    public void setImageDTO(ImageDTO[] imageDTO) {
        this.imageDTO = imageDTO;
    }
}