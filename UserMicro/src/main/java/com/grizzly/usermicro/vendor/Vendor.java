package com.grizzly.usermicro.vendor;

import com.grizzly.usermicro.user.User;

import javax.persistence.*;

@Entity(name="vendor")
@Table(name = "vendor")
public class Vendor extends User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")      private String userId;

    @AttributeOverrides({
            @AttributeOverride(name="name", column=@Column(name="name")),
            @AttributeOverride(name="contact_num", column=@Column(name="contact_num")),
            @AttributeOverride(name="email", column=@Column(name="email"))
    })
    @Column(name="address")      private String address;
    @Column(name="website")      private String website;
    @Column(name="about")        private String about;
    @Column(name="product_portfolio")  private String productPortfolio;


    public Vendor() {
        super();
    }

    public Vendor(String name, String contact_num, String email, String address, String website, String about, String product_portfolio) {
        super(name, contact_num, email);

        setAddress(address);
        setWebsite(website);
        setAbout(about);
        setProductPortfolio(product_portfolio);
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
