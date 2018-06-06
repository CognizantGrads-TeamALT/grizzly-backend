package com.grizzly.categorymicro;

import javax.persistence.*;

@Entity(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")     private Integer categoryId;

    @Column(name="name")            private String name;
    @Column(name="description")     private String description;
    @Column(name="enabled")         private boolean enabled;
    @Column(name="product_count")   private int productCount;

    public Category() {
        super();
    }

    public Category(String name, String description) {
        super();

        this.name = name;
        this.description = description;
        this.enabled = true;
        this.productCount = 0;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}