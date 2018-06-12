package com.grizzly.productmicro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Integer productId;
    private String name;
    private Integer vendorId;
    private Integer categoryId;
    private String desc;
    private Integer price;
    private Integer rating;
    private Boolean enabled;



    public Product toEntity() {
        Product product = new Product();
        product.setProductId(productId);

        product.setName(name);
        product.setVendorId(vendorId);
        product.setCategoryId(categoryId);
        product.setDesc(desc);
        product.setPrice(price);
        product.setEnabled(enabled);
        return product;
    }

    public ProductDTO(String name, Integer vendorId, Integer categoryId, String desc, Integer price, Integer rating, Boolean enabled) {
        super();

        this.name = name;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.desc = desc;
        this.price = price;
        this.rating = rating;
        this.enabled = enabled;
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

    public Integer getRating() { return rating; }

    public void setRating(Integer rating) { this.rating = rating; }

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
