package com.jovana.service.integration;

import com.jovana.entity.coupon.Coupon;
import com.jovana.entity.coupon.CouponStatus;
import com.jovana.entity.coupon.dto.CouponRequest;
import com.jovana.entity.coupon.dto.CouponResponse;
import com.jovana.repositories.coupon.CouponRepository;
import com.jovana.service.impl.coupon.CouponService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import com.jovana.service.util.RequestTestDataProvider;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by jovana on 18.04.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.coupon"})
public class CouponServiceImplIT extends AbstractTest {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;

    @DisplayName("When we want to get one or more coupons")
    @Nested
    class GetCouponTest {

        @WithMockCustomUser
        @DisplayName("Then Coupon is fetched from database when id is valid")
        @Test
        public void testGetCouponById() {
            // prepare
            Long TEST_COUPON_ID = 10L;
            // exercise
            Coupon coupon = couponService.getCouponById(TEST_COUPON_ID);
            // verify
            assertNotNull(coupon, "Coupon is null");
        }

        @WithMockCustomUser
        @DisplayName("Then all coupons are fetched from database if there are any")
        @Test
        public void testViewUserCouponsSuccess() {
            // prepare
            Long TEST_USER_ID = 11L;
            // exercise
            Set<CouponResponse> coupons = couponService.viewAllUserCoupons(TEST_USER_ID);
            // verify
            assertNotNull(coupons);
            assertEquals(2, coupons.size());
        }

    }

    @WithMockCustomUser
    @DisplayName("When we want to add coupon for a user")
    @Nested
    class AddCouponTest {

        @WithMockCustomUser(username = "admin", authorities = {"ROLE_ADMIN"})
        @DisplayName("Then coupon is created for user when valid CouponRequest is passed")
        @Test
        public void testAddCouponSuccess() {
            // prepare
            Long TEST_USER_ID = 10L;
            CouponRequest couponRequest = RequestTestDataProvider.getCouponRequests().get("john");
            Set<Coupon> couponsBefore = couponRepository.findAllByUserId(TEST_USER_ID);

            // exercise
            Long couponId = couponService.addUserCoupon(couponRequest);

            // verify
            Coupon newCoupon = couponService.getCouponById(couponId);
            Set<Coupon> couponsAfter = couponRepository.findAllByUserId(TEST_USER_ID);

            assertAll("Verify new coupon",
                    () -> assertNotNull(newCoupon),
                    () -> assertNotNull(newCoupon.getUser()),
                    () -> assertEquals(couponRequest.getUserId(), newCoupon.getUser().getId()),
                    () -> assertNotNull(newCoupon.getCode()),
                    () -> assertEquals(12, newCoupon.getCode().length()),
                    () -> assertNotNull(newCoupon.getValue()),
                    () -> assertEquals(couponRequest.getValue(), newCoupon.getValue().name()),
                    () -> assertNotNull(newCoupon.getStatus()),
                    () -> assertEquals(CouponStatus.ACTIVE, newCoupon.getStatus()),
                    () -> assertNotNull(newCoupon.getExpiryDate()),
                    () -> assertEquals(couponRequest.getExpiryDate().getDayOfMonth(), newCoupon.getExpiryDate().getDayOfMonth()),
                    () -> assertEquals(couponRequest.getExpiryDate().getMonth(), newCoupon.getExpiryDate().getMonth()),
                    () -> assertEquals(couponRequest.getExpiryDate().getYear(), newCoupon.getExpiryDate().getYear()),
                    () -> assertEquals(couponRequest.getExpiryDate().getHour(), newCoupon.getExpiryDate().getHour()),
                    () -> assertEquals(couponRequest.getExpiryDate().getMinute(), newCoupon.getExpiryDate().getMinute())
            );
            assertEquals(1, couponsAfter.size() - couponsBefore.size());
        }
    }

}
