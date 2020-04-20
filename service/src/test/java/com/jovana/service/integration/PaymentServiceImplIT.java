package com.jovana.service.integration;

import com.jovana.entity.order.payment.OrderPaymentFailedException;
import com.jovana.entity.order.payment.PaymentResponse;
import com.jovana.service.impl.payment.PaymentService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jovana on 20.04.2020
 */
public class PaymentServiceImplIT extends AbstractTest {

    @Autowired
    private PaymentService paymentService;

    @Disabled("Disabled to skip calling Stripe API every time app is built")
    @DisplayName("When we want to pay an order")
    @Nested
    class PayOrderTest {

        @WithMockCustomUser
        @DisplayName("Then payment is processed by Stripe when chargeParams are valid")
        @Test
        public void testPayOrderSuccess() {
            // prepare
            Long TEST_AMOUNT = 1L;
            String TEST_DESCRIPTION = "Test payment";

            // exercise
            PaymentResponse paymentResponse = paymentService.payOrder(TEST_AMOUNT, TEST_DESCRIPTION);

            // verify
            assertAll("Verify payment response",
                    () -> assertNotNull(paymentResponse),
                    () -> assertTrue(paymentResponse.getOrderPaid()),
                    () -> assertNotNull(paymentResponse.getStripePaymentId()),
                    () -> assertNotNull(paymentResponse.getReceiptUrl()),
                    () -> assertFalse(paymentResponse.getReceiptUrl().isEmpty())
            );
        }

        @WithMockCustomUser
        @DisplayName("Then error is thrown when processing payment fails when amount iz zero")
        @Test
        public void testPayOrderFailsWhenAmountIsZero() {
            // prepare
            Long TEST_AMOUNT = 0L;
            String TEST_DESCRIPTION = "Test payment";
            // verify
            assertThrows(OrderPaymentFailedException.class,
                    () -> paymentService.payOrder(TEST_AMOUNT, TEST_DESCRIPTION), "Failed to process payment");
        }

    }

}
