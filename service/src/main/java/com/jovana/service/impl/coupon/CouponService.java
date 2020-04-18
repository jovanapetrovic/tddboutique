package com.jovana.service.impl.coupon;

import com.jovana.entity.coupon.Coupon;
import com.jovana.entity.coupon.dto.CouponRequest;
import com.jovana.entity.coupon.dto.CouponResponse;

import java.util.Set;

/**
 * Created by jovana on 18.04.2020
 */
public interface CouponService {

    Coupon getCouponById(Long couponId);

    Long addUserCoupon(CouponRequest couponRequest);

    Set<CouponResponse> viewAllUserCoupons(Long userId);

    Long redeemCoupon(Long userId, String couponCode);

}
