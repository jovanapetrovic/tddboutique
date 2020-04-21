package com.jovana.entity.order.payment;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by jovana on 20.04.2020
 */
@ResponseStatus(BAD_REQUEST)
@ErrorCode(ExceptionCode.ORDER_PAYMENT_FAILED)
public class OrderPaymentFailedException extends TddBoutiqueApiException {

    private final BigDecimal amount;
    private final String description;

    public OrderPaymentFailedException(BigDecimal amount, String description, String message) {
        super(message);
        this.amount = amount;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

}
