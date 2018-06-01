package com.grizzly.productmicro;

import javax.persistence.*;

@Entity(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")      private String productId;

    @Column(name="name")            private String name;
    @Column(name="vendor_id")       private String vendorId;
    @Column(name="category_id")     private String categoryId;
    @Column(name="desc")            private String desc;
    @Column(name="price")           private Integer price;
    @Column(name="enabled")         private Boolean enabled;

    public Product() {
        super();
    }

    public Product(String name, String vendorId, String categoryId, String desc, Integer price, Boolean enabled) {
        setName(name);
        setVendorId(vendorId);
        setCategoryId(categoryId);
        setDesc(desc);
        setPrice(price);
        setEnabled(enabled);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}