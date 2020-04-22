package com.jovana.entity.order.dto;

import com.jovana.entity.order.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by jovana on 22.04.2020
 */
public class OrderResponse {

    private Long orderId;
    private LocalDateTime dateCreated;
    private BigDecimal totalPrice;
    private BigDecimal priceWithDiscount;
    private String paymentStatusDescription;
    private String linkToPaymentReceipt;

    public OrderResponse() {
    }

    public static OrderResponse createFromOrder(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setDateCreated(order.getCreatedDate());
        response.setTotalPrice(order.getTotalPrice());
        response.setPriceWithDiscount(order.getPriceWithDiscount());
        response.setPaymentStatusDescription(order.getPaymentStatus().getDescription());
        response.setLinkToPaymentReceipt(order.getLinkToPaymentReceipt());
        return response;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getPriceWithDiscount() {
        return priceWithDiscount;
    }

    public void setPriceWithDiscount(BigDecimal priceWithDiscount) {
        this.priceWithDiscount = priceWithDiscount;
    }

    public String getPaymentStatusDescription() {
        return paymentStatusDescription;
    }

    public void setPaymentStatusDescription(String paymentStatusDescription) {
        this.paymentStatusDescription = paymentStatusDescription;
    }

    public String getLinkToPaymentReceipt() {
        return linkToPaymentReceipt;
    }

    public void setLinkToPaymentReceipt(String linkToPaymentReceipt) {
        this.linkToPaymentReceipt = linkToPaymentReceipt;
    }

}
