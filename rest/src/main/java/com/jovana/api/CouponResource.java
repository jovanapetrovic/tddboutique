package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.coupon.dto.CouponRequest;
import com.jovana.entity.coupon.dto.CouponResponse;
import com.jovana.service.impl.coupon.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

/**
 * Created by jovana on 18.04.2020
 */
@RestController
@RequestMapping(value = PathConstants.API)
public class CouponResource {

    @Autowired
    private CouponService couponService;

    @PostMapping(value = PathConstants.COUPON_ADD)
    public ResponseEntity<Void> addUserCouponPOST(@Valid @RequestBody CouponRequest couponRequest) {
        couponService.addUserCoupon(couponRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = PathConstants.COUPON_VIEW_ALL)
    public ResponseEntity<Set<CouponResponse>> viewAllUserCouponsGET(@PathVariable("userId") Long userId) {
        Set<CouponResponse> coupons = couponService.viewAllUserCoupons(userId);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

}
