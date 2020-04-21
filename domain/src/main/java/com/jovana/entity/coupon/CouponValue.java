package com.jovana.entity.coupon;

/**
 * Created by jovana on 18.04.2020
 */
public enum CouponValue {

    COUPON_10("10% off total price"),
    COUPON_20("20% off total price");
//    COUPON_3_FOR_2("Buy 2, get 3 items");

    private String description;

    CouponValue(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
