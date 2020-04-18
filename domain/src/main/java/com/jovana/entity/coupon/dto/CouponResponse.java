package com.jovana.entity.coupon.dto;

import com.jovana.entity.coupon.Coupon;

import java.time.LocalDateTime;

/**
 * Created by jovana on 18.04.2020
 */
public class CouponResponse {

    private String code;
    private String value;
    private String status;
    private LocalDateTime expiryDate;

    public CouponResponse() {
    }

    public static CouponResponse createFromCoupon(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.setCode(coupon.getCode());
        response.setValue(coupon.getValue().getDescription());
        response.setStatus(coupon.getStatus().name());
        response.setExpiryDate(coupon.getExpiryDate());
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

}
