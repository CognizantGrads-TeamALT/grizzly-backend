package com.grizzly.productmicro.model;

import javax.persistence.*;

@Entity(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")      private Integer productId;

    @Column(name="name")            private String name;
    @Column(name="vendor_id")       private Integer vendorId;
    @Column(name="category_id")     private Integer categoryId;
    @Column(name="description",    columnDefinition = "TEXT")
                                    private String desc;
    @Column(name="price")           private Integer price;
    @Column(name="rating")          private Integer rating;
    @Column(name="enabled")         private Boolean enabled;

    @Column(name="stock", columnDefinition = "INT DEFAULT 0")   private Integer stock   = 0;
    @Column(name="req", columnDefinition = "INT DEFAULT 0")     private Integer req     = 0;
    @Column(name="buffer", columnDefinition = "INT DEFAULT 0")  private Integer buffer  = 0;
    @Column(name="pending", columnDefinition = "INT DEFAULT 0") private Integer pending = 0;

    public Product() {
        super();
    }

    public Product(String name, Integer vendorId, Integer categoryId, String desc, Integer price, Integer rating, Boolean enabled) {
        super();

        setName(name);
        setVendorId(vendorId);
        setCategoryId(categoryId);
        setDesc(desc);
        setPrice(price);
        setRating(rating);
        setEnabled(enabled);
        setStock(0);
        setReq(0);
        setBuffer(0);
        setPending(0);
    }

    public Integer getStock() { return stock; }

    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getReq() { return req; }

    public void setReq(Integer req) { this.req = req; }

    public Integer getBuffer() { return buffer; }

    public void setBuffer(Integer buffer) { this.buffer = buffer; }

    public Integer getPending() { return pending; }

    public void setPending(Integer pending) { this.pending = pending; }

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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}