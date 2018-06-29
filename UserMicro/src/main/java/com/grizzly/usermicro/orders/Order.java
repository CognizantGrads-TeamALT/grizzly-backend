package com.grizzly.usermicro.orders;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="user_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer order_id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "txn_id")
    private Integer txn_id;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "destination")
    private String destination;

    @Column(name = "shipped_on")
    private java.time.LocalDate shipped_on;

    public Order() {

    }

    public Order(Integer user_id, Integer txn_id, Double cost, String destination, LocalDate shipped_on) {
        this.user_id = user_id;
        this.txn_id = txn_id;
        this.cost = cost;
        this.destination = destination;
        this.shipped_on = shipped_on;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(Integer txn_id) {
        this.txn_id = txn_id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getShipped_on() {
        return shipped_on;
    }

    public void setShipped_on(LocalDate shipped_on) {
        this.shipped_on = shipped_on;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
