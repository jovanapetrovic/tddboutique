package com.jovana.entity.order.dto;

import com.jovana.entity.order.OrderItem;

import java.math.BigDecimal;

/**
 * Created by jovana on 20.04.2020
 */
public class OrderItemResponse {

    private Long orderItemId;
    private Long productId;
    private String productName;
    private String productSize;
    private String productColor;
    private Long quantity;
    private BigDecimal productPrice;
    private BigDecimal totalPricePerProduct;

    public OrderItemResponse() {
    }

    public static OrderItemResponse createFromOrderItem(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setOrderItemId(orderItem.getId());
        response.setProductId(orderItem.getProduct().getId());
        response.setProductName(orderItem.getProduct().getName());
        response.setProductSize(orderItem.getProductSize().getCode());
        response.setProductColor(orderItem.getProductColor().getCode());
        response.setQuantity(orderItem.getQuantity());
        response.setProductPrice(orderItem.getProduct().getPrice());
        response.setTotalPricePerProduct(orderItem.getTotalPricePerProduct());
        return response;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getTotalPricePerProduct() {
        return totalPricePerProduct;
    }

    public void setTotalPricePerProduct(BigDecimal totalPricePerProduct) {
        this.totalPricePerProduct = totalPricePerProduct;
    }

}
