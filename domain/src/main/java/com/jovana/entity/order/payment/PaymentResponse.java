package com.jovana.entity.order.payment;

/**
 * Created by jovana on 20.04.2020
 */
public class PaymentResponse {

    private Boolean isOrderPaid;
    private String stripePaymentId;
    private String receiptUrl;

    public PaymentResponse() {
    }

    public PaymentResponse(Boolean isOrderPaid, String stripePaymentId, String receiptUrl) {
        this.isOrderPaid = isOrderPaid;
        this.stripePaymentId = stripePaymentId;
        this.receiptUrl = receiptUrl;
    }

    public Boolean getOrderPaid() {
        return isOrderPaid;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

}
