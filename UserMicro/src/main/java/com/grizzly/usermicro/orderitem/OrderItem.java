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
    private String productId;

    @Column(name ="rating")
    private String rating;

    @Column(name ="quantity")
    private String quantity;

    public OrderItem() {
    }

    public OrderItem(Integer order_id, String productId, String rating, String quantity) {
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
