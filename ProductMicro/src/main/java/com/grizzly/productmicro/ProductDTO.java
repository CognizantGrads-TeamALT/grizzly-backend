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
    private String productId;
    private String name;
    private String vendorId;
    private String categoryId;
    private String desc;
    private Integer price;

    public Product toEntity() {
        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setVendorId(vendorId);
        product.setCategoryId(categoryId);
        product.setDesc(desc);
        product.setPrice(price);
        return product;
    }
}
