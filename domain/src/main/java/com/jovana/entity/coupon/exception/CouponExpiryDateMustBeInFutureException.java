package com.jovana.entity.coupon.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by jovana on 11.04.2020
 */
@ResponseStatus(BAD_REQUEST)
@ErrorCode(ExceptionCode.COUPON_EXPIRY_DATE_MUST_BE_IN_FUTURE)
public class CouponExpiryDateMustBeInFutureException extends TddBoutiqueApiException {

    private final LocalDateTime expiryDate;

    public CouponExpiryDateMustBeInFutureException(LocalDateTime expiryDate, String message) {
        super(message);
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
