package com.jovana.entity.order;

/**
 * Created by jovana on 20.04.2020
 */
public enum PaymentStatus {

    PAID("Payment by card"),
    DELIVERY("Payment on delivery");

    private String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
