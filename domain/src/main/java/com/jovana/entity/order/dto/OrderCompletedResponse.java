package com.jovana.entity.order.dto;

import com.jovana.entity.order.PaymentStatus;

import java.math.BigDecimal;

/**
 * Created by jovana on 20.04.2020
 */
public class OrderCompletedResponse {

    private BigDecimal totalPrice;
    private BigDecimal totalPriceWithDiscount;
    private String usedCouponCode;
    private String paymentStatusDescription;
    private String receiptUrl;

    public OrderCompletedResponse() {
    }

    public OrderCompletedResponse(BigDecimal totalPrice, BigDecimal totalPriceWithDiscount,
                                  String usedCouponCode, boolean isCardPayment, String receiptUrl) {
        this.totalPrice = totalPrice;
        this.totalPriceWithDiscount = totalPriceWithDiscount;
        this.usedCouponCode = usedCouponCode;
        this.paymentStatusDescription = isCardPayment ? PaymentStatus.PAID.getDescription() : PaymentStatus.DELIVERY.getDescription();
        this.receiptUrl = receiptUrl;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPriceWithDiscount() {
        return totalPriceWithDiscount;
    }

    public void setTotalPriceWithDiscount(BigDecimal totalPriceWithDiscount) {
        this.totalPriceWithDiscount = totalPriceWithDiscount;
    }

    public String getUsedCouponCode() {
        return usedCouponCode;
    }

    public void setUsedCouponCode(String usedCouponCode) {
        this.usedCouponCode = usedCouponCode;
    }

    public String getPaymentStatusDescription() {
        return paymentStatusDescription;
    }

    public void setPaymentStatusDescription(String paymentStatusDescription) {
        this.paymentStatusDescription = paymentStatusDescription;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

}
