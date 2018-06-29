package com.grizzly.productmicro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryDTO {
    private Integer productId;
    private String name;
    private Integer stock;
    private Integer req;
    private Integer buffer;
    private Integer pending;
    private Integer price;
    private Integer rating;

    public ProductInventoryDTO(String name, Integer stock, Integer req, Integer buffer, Integer pending, Integer price, Integer rating) {
        super();

        this.name = name;
        this.stock = stock;
        this.req = req;
        this.buffer = buffer;
        this.pending = pending;
        this.price = price;
        this.rating = rating;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getReq() {
        return req;
    }

    public void setReq(Integer req) {
        this.req = req;
    }

    public Integer getBuffer() {
        return buffer;
    }

    public void setBuffer(Integer buffer) {
        this.buffer = buffer;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
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
}
