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
    private Boolean enabled;
    private ImageDTO[] imageDTO;

    public Product toEntity() {
        return new Product(this.name, this.vendorId, this.categoryId, this.desc, this.price, this.enabled);
    }

    public ProductDTO(String name, Integer vendorId, Integer categoryId, String desc, Integer price, Boolean enabled, ImageDTO[] imageDTO) {
        super();

        this.name = name;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.desc = desc;
        this.price = price;
        this.enabled = enabled;
        this.imageDTO = imageDTO;
    }

}
