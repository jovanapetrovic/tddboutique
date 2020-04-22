package com.jovana.entity.order.dto;

import com.google.common.collect.Sets;
import com.jovana.entity.order.Order;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.shippingaddress.dto.ShippingAddressResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by jovana on 22.04.2020
 */
public class OrderFullResponse {

    private Long orderId;
    private LocalDateTime dateCreated;
    private String note;
    private String couponCodeUsed;
    private String couponValueDescription;
    private BigDecimal totalPrice;
    private BigDecimal priceWithDiscount;
    private String paymentStatusDescription;
    private String linkToPaymentReceipt;
    private ShippingAddressResponse shippingAddress;
    private Set<OrderItemResponse> orderItems;

    public OrderFullResponse() {
    }

    // used by Hibernate
    public OrderFullResponse(Order order) {
        this.orderId = order.getId();
        this.dateCreated = order.getCreatedDate();
        this.note = order.getNote();
        if (order.getCoupon() != null) {
            this.couponCodeUsed = order.getCoupon().getCode();
            this.couponValueDescription = order.getCoupon().getValue().getDescription();
        }
        this.totalPrice = order.getTotalPrice();
        this.priceWithDiscount = order.getPriceWithDiscount();
        this.paymentStatusDescription = order.getPaymentStatus().getDescription();
        this.linkToPaymentReceipt = order.getLinkToPaymentReceipt();

        this.shippingAddress = ShippingAddressResponse.createFromShippingAddress(order.getShippingAddress());

        Set<OrderItemResponse> orderItems = Sets.newHashSet();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse response = OrderItemResponse.createFromOrderItem(item);
            orderItems.add(response);
        }
        this.orderItems = orderItems;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCouponCodeUsed() {
        return couponCodeUsed;
    }

    public void setCouponCodeUsed(String couponCodeUsed) {
        this.couponCodeUsed = couponCodeUsed;
    }

    public String getCouponValueDescription() {
        return couponValueDescription;
    }

    public void setCouponValueDescription(String couponValueDescription) {
        this.couponValueDescription = couponValueDescription;
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

    public ShippingAddressResponse getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressResponse shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Set<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

}
