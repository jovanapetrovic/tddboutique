package com.jovana.entity.order.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jovana on 20.04.2020
 */
public class CheckoutCartRequest {

    @Size(max = 500)
    private String note;

    @Size(max = 12)
    private String couponCode;

    @NotNull
    private String paymentMethod;

    @NotNull
    private Long shippingAddressId;

    public CheckoutCartRequest() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

}
