package com.jovana.repositories.coupon;

import com.jovana.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by jovana on 18.04.2020
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Set<Coupon> findAllByUserId(Long userId);

    Coupon findByUserIdAndCode(Long userId, String code);

}
