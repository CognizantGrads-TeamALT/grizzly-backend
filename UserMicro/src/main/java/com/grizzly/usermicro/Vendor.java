package com.grizzly.usermicro;

import javax.persistence.*;

@Entity(name="vendor")
public class Vendor extends User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")      private String userId;

    @Column(name="address")      private String address;
    @Column(name="website")      private String website;
    @Column(name="about")        private String about;
    @Column(name="product_portfolio")  private String productPortfolio;


    public Vendor() {
        super();
    }

    public Vendor(String address, String website, String about, String productPortfolio) {
        super();

        setAddress(address);
        setWebsite(website);
        setAbout(about);
        setProductPortfolio(productPortfolio);
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
