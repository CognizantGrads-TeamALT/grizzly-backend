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
}
