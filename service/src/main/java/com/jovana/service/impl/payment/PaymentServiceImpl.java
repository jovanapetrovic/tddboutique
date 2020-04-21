package com.jovana.service.impl.payment;

import com.jovana.entity.order.payment.OrderPaymentFailedException;
import com.jovana.entity.order.payment.PaymentResponse;
import com.jovana.service.security.IsUser;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by jovana on 20.04.2020
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private static final String CURRENCY = "eur";
    private static final String SOURCE = "tok_visa"; // test Visa card, value can be "tok_mastercard" too

    @Autowired
    private String getStripeApiKey;

    @IsUser
    @Override
    public PaymentResponse payOrder(BigDecimal amount, String description) {
        Stripe.apiKey = getStripeApiKey;

        Long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

        ChargeCreateParams chargeParams =
                ChargeCreateParams.builder()
                        .setAmount(amountInCents) // amount is in cents
                        .setCurrency(CURRENCY)
                        .setDescription(description)
                        .setSource(SOURCE)
                        .build();

        Charge charge;
        try {
            charge = Charge.create(chargeParams);
            LOGGER.info("Payment successful. Amount charged = {}", charge.getAmount());
        } catch (StripeException e) {
            LOGGER.debug("Exception occurred on charge: stripeError = {}, requestId = {}", e.getStripeError(), e.getRequestId());
            throw new OrderPaymentFailedException(amount, description, "Failed to process payment for this order.");
        }

        return new PaymentResponse(charge.getPaid(), charge.getId(), charge.getReceiptUrl());
    }
}
