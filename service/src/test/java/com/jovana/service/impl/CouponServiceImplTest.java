package com.jovana.service.impl;

import com.jovana.entity.coupon.Coupon;
import com.jovana.entity.coupon.CouponStatus;
import com.jovana.entity.coupon.CouponValue;
import com.jovana.entity.coupon.dto.CouponRequest;
import com.jovana.entity.coupon.dto.CouponResponse;
import com.jovana.entity.coupon.exception.CouponAlreadyRedeemedException;
import com.jovana.entity.coupon.exception.CouponExpiredException;
import com.jovana.entity.coupon.exception.CouponExpiryDateMustBeInFutureException;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.coupon.CouponRepository;
import com.jovana.service.impl.coupon.CouponService;
import com.jovana.service.impl.coupon.CouponServiceImpl;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.util.RequestTestDataProvider;
import com.jovana.service.util.TestDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Created by jovana on 18.04.2020
 */
@ExtendWith(MockitoExtension.class)
public class CouponServiceImplTest {

    @InjectMocks
    private CouponService couponService = new CouponServiceImpl();
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserService userService;

    @DisplayName("When we want to get one or more coupons")
    @Nested
    class GetAndViewCouponTest {

        private final Long TEST_COUPON_ID = 10L;
        private final Long COUPON_ID_NOT_EXISTS = 9999L;

        @DisplayName("Then Coupon is fetched from database when id is valid")
        @Test
        public void testGetProductById() {
            // prepare
            when(couponRepository.findById(anyLong())).thenReturn(Optional.of(mock(Coupon.class)));
            // exercise
            Coupon coupon = couponService.getCouponById(TEST_COUPON_ID);
            // verify
            assertNotNull(coupon, "Coupon is null");
        }

        @DisplayName("Then error is thrown when Coupon with passed id doesn't exist")
        @Test
        public void testGetCouponByIdFailsWhenPassedIdDoesntExist() {
            // prepare
            when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());
            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> couponService.getCouponById(COUPON_ID_NOT_EXISTS), "Coupon with id=" + COUPON_ID_NOT_EXISTS + " doesn't exist");
        }

        @DisplayName("Then all coupons are fetched from database if there are any")
        @Test
        public void testViewAllUserCouponsSuccess() {
            // prepare
            Long TEST_USER_ID = 10L;

            Coupon couponMock1 = mock(Coupon.class);
            Coupon couponMock2 = mock(Coupon.class);
            Coupon couponMock3 = mock(Coupon.class);
            Set<Coupon> coupons = Sets.newSet(couponMock1, couponMock2, couponMock3);

            when(couponRepository.findAllByUserId(TEST_USER_ID)).thenReturn(coupons);

            when(couponMock1.getValue()).thenReturn(CouponValue.COUPON_10);
            when(couponMock2.getValue()).thenReturn(CouponValue.COUPON_20);
            when(couponMock3.getValue()).thenReturn(CouponValue.COUPON_10);

            when(couponMock1.getStatus()).thenReturn(CouponStatus.ACTIVE);
            when(couponMock2.getStatus()).thenReturn(CouponStatus.REDEEMED);
            when(couponMock3.getStatus()).thenReturn(CouponStatus.ACTIVE);

            // exercise
            Set<CouponResponse> couponResponses = couponService.viewAllUserCoupons(TEST_USER_ID);

            // verify
            assertNotNull(couponResponses);
            assertEquals(3, couponResponses.size());
        }
    }

    @DisplayName("When we want to add a new coupon")
    @Nested
    class AddCouponTest {

        private CouponRequest johnCouponRequest;
        private Coupon johnCoupon;
        private User johnUser;

        @BeforeEach
        void setUp() {
            johnCouponRequest = RequestTestDataProvider.getCouponRequests().get("john");
            johnCoupon = TestDataProvider.getCoupons().get("john");
            johnUser = TestDataProvider.getUsers().get("john");
        }

        @DisplayName("Then coupon is created when valid CouponRequest is passed")
        @Test
        public void testAddUserCouponSuccess() {
            // prepare
            when(userService.getUserById(anyLong())).thenReturn(johnUser);
            when(couponRepository.save(any(Coupon.class))).thenReturn(johnCoupon);

            when(couponRepository.findById(anyLong())).thenReturn(Optional.of(johnCoupon));

            // exercise
            Long couponId = couponService.addUserCoupon(johnCouponRequest);

            // verify
            Coupon newCoupon = couponService.getCouponById(couponId);

            assertAll("Verify new coupon",
                    () -> assertNotNull(newCoupon),
                    () -> assertNotNull(newCoupon.getId()),
                    () -> assertNotNull(newCoupon.getUser()),
                    () -> assertEquals(johnUser.getId(), newCoupon.getUser().getId()),
                    () -> assertNotNull(newCoupon.getCode()),
                    () -> assertNotNull(newCoupon.getValue()),
                    () -> assertEquals(johnCouponRequest.getValue(), johnCoupon.getValue().name()),
                    () -> assertNotNull(newCoupon.getStatus()),
                    () -> assertNotEquals(CouponStatus.ACTIVE.name(), newCoupon.getStatus())
            );
            verify(couponRepository, times(1)).save(any(Coupon.class));
        }

        @DisplayName("Then error is thrown when coupon expiry date is in the past")
        @Test
        public void testAddCouponFailsWhenCouponExpiryDateIsInPast() {
            // prepare
            User userMock = mock(User.class);
            CouponRequest couponRequestMock = mock(CouponRequest.class);

            when(userService.getUserById(anyLong())).thenReturn(userMock);
            when(couponRequestMock.getExpiryDate()).thenReturn(LocalDateTime.now().minusDays(1));

            // verify
            assertThrows(CouponExpiryDateMustBeInFutureException.class,
                    () -> couponService.addUserCoupon(couponRequestMock));
            verify(couponRepository, times(0)).save(any(Coupon.class));
        }

    }

    @DisplayName("When we want to check if a coupon is valid")
    @Nested
    class CheckIfCouponIsValidTest {

        private final Long TEST_USER_ID = 10L;
        private final String TEST_COUPON_CODE = "ASDF5678asdf";

        private Coupon johnCoupon;
        private Coupon redeemedCoupon;
        private Coupon johnExpiredCoupon;

        @BeforeEach
        void setUp() {
            johnCoupon = TestDataProvider.getCoupons().get("john");
            redeemedCoupon = TestDataProvider.getCoupons().get("johnRedeemed");
            johnExpiredCoupon = TestDataProvider.getCoupons().get("johnExpired");
        }

        @DisplayName("Then coupon is considered active when valid couponCode is passed")
        @Test
        public void testCheckIfCouponIsValidSuccess() {
            // prepare
            when(couponRepository.findByUserIdAndCode(anyLong(), anyString())).thenReturn(johnCoupon);
            // exercise
            Coupon coupon = couponService.checkIfCouponIsValid(TEST_USER_ID, TEST_COUPON_CODE);
            // verify
            assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
        }

        @DisplayName("Then error is thrown when coupon with passed couponCode doesn't exist for user")
        @Test
        public void testCheckIfCouponIsValidFailsWhenItDoesntExistInDb() {
            // prepare
            when(couponRepository.findByUserIdAndCode(anyLong(), anyString())).thenReturn(null);

            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> couponService.checkIfCouponIsValid(TEST_USER_ID, TEST_COUPON_CODE));
            verify(couponRepository, times(0)).save(any(Coupon.class));
        }

        @DisplayName("Then error is thrown when coupon is already redeemed")
        @Test
        public void testCheckIfCouponIsValidFailsWhenCouponIsAlreadyRedeemed() {
            // prepare
            when(couponRepository.findByUserIdAndCode(anyLong(), anyString())).thenReturn(redeemedCoupon);

            // verify
            assertThrows(CouponAlreadyRedeemedException.class,
                    () -> couponService.checkIfCouponIsValid(TEST_USER_ID, TEST_COUPON_CODE));
            verify(couponRepository, times(0)).save(any(Coupon.class));
        }

        @DisplayName("Then error is thrown when coupon has already expired")
        @Test
        public void testCheckIfCouponIsValidFailsWhenCouponHasExpired() {
            // prepare
            when(couponRepository.findByUserIdAndCode(anyLong(), anyString())).thenReturn(johnExpiredCoupon);

            // verify
            assertThrows(CouponExpiredException.class,
                    () -> couponService.checkIfCouponIsValid(TEST_USER_ID, TEST_COUPON_CODE));
            verify(couponRepository, times(0)).save(any(Coupon.class));
        }

    }

    @DisplayName("When we want to redeem a coupon")
    @Nested
    class RedeemCouponTest {

        @DisplayName("Then coupon is redeemed when coupon is passed")
        @Test
        public void testRedeemCouponSuccess() {
            // prepare
            Coupon johnCoupon = TestDataProvider.getCoupons().get("john");
            // exercise
            boolean isRedeemed = couponService.redeemCoupon(johnCoupon);
            // verify
            assertTrue(isRedeemed);
        }

    }

    @DisplayName("When we want to calculate a coupon discount")
    @Nested
    class CalculatePriceWithDiscountTest {

        @DisplayName("Then a discount of 10% is applied when")
        @Test
        public void testCalculatePriceWith10PCTDiscountSuccess() {
            // prepare
            BigDecimal originalTotalPrice = new BigDecimal("74.99");
            BigDecimal expectedDiscountedPrice = new BigDecimal("67.49");
            Coupon johnCoupon = TestDataProvider.getCoupons().get("john");
            // exercise
            BigDecimal priceWithDiscount = couponService.calculatePriceWithDiscount(johnCoupon, originalTotalPrice);
            // verify
            assertNotNull(priceWithDiscount);
            assertEquals(expectedDiscountedPrice, priceWithDiscount);
        }

        @DisplayName("Then a discount of 20% is applied when")
        @Test
        public void testCalculatePriceWith20PCTDiscountSuccess() {
            // prepare
            BigDecimal originalTotalPrice = new BigDecimal("74.99");
            BigDecimal expectedDiscountedPrice = new BigDecimal("59.99");
            Coupon johnCoupon = TestDataProvider.getCoupons().get("active20Coupon");
            // exercise
            BigDecimal priceWithDiscount = couponService.calculatePriceWithDiscount(johnCoupon, originalTotalPrice);
            // verify
            assertNotNull(priceWithDiscount);
            assertEquals(expectedDiscountedPrice, priceWithDiscount);
        }

    }

}
