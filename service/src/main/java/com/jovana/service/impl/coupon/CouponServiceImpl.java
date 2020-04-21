package com.jovana.service.impl.coupon;

import com.google.common.collect.Sets;
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
import com.jovana.service.impl.user.UserService;
import com.jovana.service.security.IsAdmin;
import com.jovana.service.security.IsAdminOrUser;
import com.jovana.service.security.IsUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * Created by jovana on 18.04.2020
 */
@Service
public class CouponServiceImpl implements CouponService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserService userService;

    @IsAdminOrUser
    @Override
    public Coupon getCouponById(Long couponId) {
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        if (coupon.isEmpty()) {
            LOGGER.info("Coupon with id = {} was not found in the db.", couponId);
            throw new EntityNotFoundException("No coupon found with id = " + couponId);
        }
        return coupon.get();
    }

    @IsAdmin
    @Override
    public Long addUserCoupon(CouponRequest couponRequest) {
        User user = userService.getUserById(couponRequest.getUserId());

        LocalDateTime expiryDate = couponRequest.getExpiryDate();
        if (expiryDate.isBefore(LocalDateTime.now())) {
            throw new CouponExpiryDateMustBeInFutureException(expiryDate, "This coupon has already expired.");
        }

        Coupon coupon = new Coupon();
        coupon.setUser(user);
        coupon.setCode(RandomStringUtils.randomAlphanumeric(12));
        coupon.setValue(CouponValue.valueOf(couponRequest.getValue()));
        coupon.setStatus(CouponStatus.ACTIVE);
        coupon.setExpiryDate(expiryDate);

        Coupon newCoupon = couponRepository.save(coupon);
        return newCoupon.getId();
    }

    @IsAdminOrUser
    @Override
    public Set<CouponResponse> viewAllUserCoupons(Long userId) {
        Set<Coupon> coupons = couponRepository.findAllByUserId(userId);

        Set<CouponResponse> couponResponses = Sets.newHashSet();
        for (Coupon coupon : coupons) {
            couponResponses.add(CouponResponse.createFromCoupon(coupon));
        }
        LOGGER.info("Found {} user coupons.", couponResponses.size());
        return couponResponses;
    }

    @IsUser
    @Override
    public Coupon checkIfCouponIsValid(Long userId, String couponCode) {
        Coupon coupon = couponRepository.findByUserIdAndCode(userId, couponCode);

        if (coupon == null) {
            LOGGER.info("Coupon with userId = {} and couponCode = {} was not found in the db.", userId, couponCode);
            throw new EntityNotFoundException("No coupon found for user with couponCode = " + couponCode);
        }
        if (coupon.getStatus() == CouponStatus.REDEEMED) {
            throw new CouponAlreadyRedeemedException(couponCode, "This coupon has already been redeemed.");
        }
        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException(couponCode, "This coupon has already expired.");
        }

        return coupon;
    }

    @IsUser
    @Override
    public boolean redeemCoupon(Coupon coupon) {
        coupon.setStatus(CouponStatus.REDEEMED);
        couponRepository.save(coupon);
        return CouponStatus.REDEEMED.equals(coupon.getStatus());
    }

    @Override
    public BigDecimal calculatePriceWithDiscount(Coupon coupon, BigDecimal originalTotalPrice) {
        BigDecimal totalPriceWithDiscount = originalTotalPrice;

        if (CouponValue.COUPON_10.equals(coupon.getValue())) {
            totalPriceWithDiscount = originalTotalPrice.subtract(percentage(originalTotalPrice, new BigDecimal("10")));
        }
        if (CouponValue.COUPON_20.equals(coupon.getValue())) {
            totalPriceWithDiscount = originalTotalPrice.subtract(percentage(originalTotalPrice, new BigDecimal("20")));
        }

        LOGGER.info("Applied coupon: " + coupon.getValue().getDescription() + ", newPrice = {}", totalPriceWithDiscount);
        return totalPriceWithDiscount;
    }

    private static BigDecimal percentage(BigDecimal base, BigDecimal pct){
        return base.multiply(pct).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN);
    }

}
