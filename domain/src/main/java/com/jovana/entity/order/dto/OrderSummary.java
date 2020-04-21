package com.jovana.entity.order.dto;

import com.jovana.entity.order.OrderItem;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by jovana on 20.04.2020
 */
public class OrderSummary {

    private Set<OrderItem> orderItems;
    private String productNames;
    private BigDecimal totalPrice;

    public OrderSummary() {
    }

    public OrderSummary(Set<OrderItem> orderItems, String productNames, BigDecimal totalPrice) {
        this.orderItems = orderItems;
        this.productNames = productNames;
        this.totalPrice = totalPrice;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}
