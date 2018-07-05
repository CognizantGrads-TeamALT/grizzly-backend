package com.grizzly.vendormicro.model;

import javax.persistence.*;

@Entity(name="vendor")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vendor_id")       private Integer vendorId;

    @Column(name="name")            private String name;
    @Column(name="contact_num")     private String contactNum;
    @Column(name="website")         private String website;
    @Column(name="email")           private String email;
    @Column(name="bio")             private String bio;
    @Column(name="enabled")         private Boolean enabled;

    public Vendor() {
        super();
    }

    public Vendor(String name, String contactNum, String website, String email, String bio, Boolean enabled) {
        super();

        setName(name);
        setContactNum(contactNum);
        setWebsite(website);
        setEmail(email);
        setBio(bio);
        setEnabled(enabled);
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
}
