package com.grizzly.productmicro;

import javax.persistence.*;

@Entity(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")      private Integer productId;

    @Column(name="name")            private String name;
    @Column(name="vendor_id")       private Integer vendorId;
    @Column(name="category_id")     private Integer categoryId;
    @Column(name="desc")            private String desc;
    @Column(name="price")           private Integer price;
    @Column(name="enabled")         private Boolean enabled;

    public Product() {
        super();
    }

    public Product(String name, Integer vendorId, Integer categoryId, String desc, Integer price, Boolean enabled) {
        super();

        setName(name);
        setVendorId(vendorId);
        setCategoryId(categoryId);
        setDesc(desc);
        setPrice(price);
        setEnabled(enabled);
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
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