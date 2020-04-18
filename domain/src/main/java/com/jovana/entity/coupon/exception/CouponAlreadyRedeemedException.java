package com.jovana.entity.coupon.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by jovana on 11.04.2020
 */
@ResponseStatus(BAD_REQUEST)
@ErrorCode(ExceptionCode.COUPON_ALREADY_REDEEMED)
public class CouponAlreadyRedeemedException extends TddBoutiqueApiException {

    private final String code;

    public CouponAlreadyRedeemedException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
