package com.grizzly.categorymicro;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="category")
public class Category {
    @Id

    @Column(name="category_id") private String categoryId;
    @Column(name="name") private String name;
    @Column(name="description") private String description;
    @Column(name="enabled") private boolean enabled;

    public Category()
    {
        super();

    }

    public Category(String categoryId, String name, String description, boolean enabled) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
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
}
