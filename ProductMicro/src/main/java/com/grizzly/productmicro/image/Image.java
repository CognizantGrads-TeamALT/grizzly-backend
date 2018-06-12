package com.grizzly.productmicro.image;

import javax.persistence.*;

@Entity(name="product_image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Integer image_id;

    @Column(name="product_id")
    private Integer product_id;

    @Column(name="image_url")
    private String image_url;

    public Image() {
    }

    public Image(Integer product_id, String image_url) {
        this.product_id = product_id;
        this.image_url = image_url;
    }

    public Integer getImage_id() {
        return image_id;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
