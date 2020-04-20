package com.jovana.service.impl.payment;

import com.jovana.entity.order.payment.PaymentResponse;

/**
 * Created by jovana on 20.04.2020
 */
public interface PaymentService {

    PaymentResponse payOrder(Long amount, String description);

}
