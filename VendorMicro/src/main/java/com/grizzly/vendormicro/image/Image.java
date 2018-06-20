package com.grizzly.vendormicro.image;

import javax.persistence.*;

@Entity(name="vendor_image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Integer image_id;

    @Column(name="vendor_id")
    private Integer vendor_id;

    @Column(name="image_url")
    private String image_url;

    public Image() {
    }

    public Image(Integer vendor_id, String image_url) {
        this.vendor_id = vendor_id;
        this.image_url = image_url;
    }

    public Integer getImage_id() {
        return image_id;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
