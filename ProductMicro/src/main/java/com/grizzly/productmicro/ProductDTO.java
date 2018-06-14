package com.grizzly.productmicro;

import com.grizzly.productmicro.image.ImageDTO;
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
    private ImageDTO[] imageDTO;



    public Product toEntity() {
        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setVendorId(vendorId);
        product.setCategoryId(categoryId);
        product.setDesc(desc);
        product.setPrice(price);
        product.setRating(rating);
        product.setEnabled(enabled);
        return product;
    }

    public ProductDTO(String name, Integer vendorId, Integer categoryId, String desc, Integer price, Integer rating, Boolean enabled, ImageDTO[] imageDTO) {
        super();

        this.name = name;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.desc = desc;
        this.price = price;
        this.rating = rating;
        this.enabled = enabled;
        this.imageDTO = imageDTO;
    }


}
