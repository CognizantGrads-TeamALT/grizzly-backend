package com.grizzly.categorymicro.model;

public class CategoryDTO {
    private Integer categoryId;
    private String name;
    private String description;
    private Boolean enabled;
    private Integer product_count;

    public CategoryDTO(String name, String description, Boolean enabled, Integer product_count) {
        super();

        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.product_count = product_count;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getProductCount() { return product_count; }

    public void setProductCount(Integer product_count) { this.product_count = product_count; }
}