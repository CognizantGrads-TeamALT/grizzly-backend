package com.grizzly.usermicro.orderitem;

import javax.persistence.*;

@Entity(name="order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer orderItemId;

    @Column(name = "order_id")
    private Integer order_id;

    @Column(name ="product_id")
    private Integer productId;

    @Column(name ="rating")
    private Float rating;

    @Column(name ="quantity")
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(Integer order_id, Integer productId, Float rating, Integer quantity) {
        this.order_id = order_id;
        this.productId = productId;
        this.rating = rating;
        this.quantity = quantity;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
