package com.grizzly.usermicro.orderitem;

public class OrderItemDTO {
    private Integer order_id;
    private Integer productId;
    private float rating;
    private Integer quantity;


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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItem toEntity(Integer orderID){
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);


        return orderItem;

    }
}
