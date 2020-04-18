package com.jovana.entity.coupon.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by jovana on 18.04.2020
 */
public class CouponRequest {

    @NotNull
    private Long userId;

    @NotNull
    private String value;

    @NotNull
    @Future
    private LocalDateTime expiryDate;

    public CouponRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

}
