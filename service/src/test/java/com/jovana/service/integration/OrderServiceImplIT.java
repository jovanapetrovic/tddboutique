package com.jovana.service.integration;

import com.jovana.entity.coupon.Coupon;
import com.jovana.entity.coupon.CouponStatus;
import com.jovana.entity.coupon.exception.CouponAlreadyRedeemedException;
import com.jovana.entity.order.Order;
import com.jovana.entity.order.PaymentStatus;
import com.jovana.entity.order.dto.*;
import com.jovana.entity.order.exception.InvalidCartSizeException;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.order.OrderRepository;
import com.jovana.service.impl.coupon.CouponService;
import com.jovana.service.impl.order.OrderItemService;
import com.jovana.service.impl.order.OrderService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jovana on 21.04.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.order"})
public class OrderServiceImplIT extends AbstractTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CouponService couponService;
    @Autowired
    private OrderItemService orderItemService;

    @DisplayName("When we want to checkout user cart and have order processed")
    @Nested
    class CheckoutCartAndProcessOrderTest {

        private final Long TEST_USER_ID = 10L;
        private final String TEST_NOTE = "Please, deliver this on work days between 9am and 5pm.";
        private final Long TEST_COUPON_ID = 11L;
        private final String TEST_COUPON_CODE = "ASDF5678asdf";
        private final String PAYMENT_METHOD_DELIVERY = "DELIVERY";
        private final String PAYMENT_METHOD_CARD = "CARD"; // switch methods if you want to call Stripe API
        private final Long TEST_SHIPPING_ADDRESS_ID = 10L;

        private final BigDecimal TOTAL_PRICE = new BigDecimal("370.00");
        private final BigDecimal TOTAL_PRICE_WITH_20PCT_DISCOUNT = new BigDecimal("296.00");

        @WithMockCustomUser
        @DisplayName("Then new order is created when valid CheckoutCartRequest is provided")
        @Test
        public void testCheckoutCartAndProcessOrderSuccess() {
            // prepare
            CheckoutCartRequest deliveryRequest = new CheckoutCartRequest();
            deliveryRequest.setNote(TEST_NOTE);
            deliveryRequest.setCouponCode(TEST_COUPON_CODE);
            deliveryRequest.setPaymentMethod(PAYMENT_METHOD_DELIVERY);
            deliveryRequest.setShippingAddressId(TEST_SHIPPING_ADDRESS_ID);

            // verify coupon before
            Coupon couponBefore = couponService.getCouponById(TEST_COUPON_ID);
            assertEquals(CouponStatus.ACTIVE, couponBefore.getStatus());

            // verify order before
            Set<OrderResponse> ordersBefore = orderService.viewUserOrders(TEST_USER_ID);
            assertEquals(0L, ordersBefore.size());

            // exercise
            OrderCompletedResponse orderCompletedResponse = orderService.checkoutCartAndProcessOrder(TEST_USER_ID, deliveryRequest);

            // verify
            assertAll("Verify orderCompletedResponse",
                    () -> assertNotNull(orderCompletedResponse),
                    () -> assertNotNull(orderCompletedResponse.getTotalPrice()),
                    () -> assertEquals(TOTAL_PRICE, orderCompletedResponse.getTotalPrice()),
                    () -> assertNotNull(orderCompletedResponse.getTotalPriceWithDiscount()),
                    () -> assertEquals(TOTAL_PRICE_WITH_20PCT_DISCOUNT, orderCompletedResponse.getTotalPriceWithDiscount()),
                    () -> assertNotNull(orderCompletedResponse.getUsedCouponCode()),
                    () -> assertEquals(TEST_COUPON_CODE, orderCompletedResponse.getUsedCouponCode()),
                    () -> assertNotNull(orderCompletedResponse.getPaymentStatusDescription()),
                    () -> assertEquals(PaymentStatus.DELIVERY.getDescription(), orderCompletedResponse.getPaymentStatusDescription()),
                    () -> assertNull(orderCompletedResponse.getReceiptUrl())
            );

            // verify coupon after
            Coupon couponAfter = couponService.getCouponById(TEST_COUPON_ID);
            assertEquals(CouponStatus.REDEEMED, couponAfter.getStatus());

            // verify orders after
            Set<OrderResponse> ordersAfter = orderService.viewUserOrders(TEST_USER_ID);
            assertEquals(ordersBefore.size() + 1, ordersAfter.size());

            Long orderId = ordersAfter.iterator().next().getOrderId();

            Order newOrder = orderRepository.findById(orderId).get();

            assertAll("Verify new order",
                    () -> assertNotNull(newOrder.getTotalPrice()),
                    () -> assertEquals(TOTAL_PRICE, newOrder.getTotalPrice()),
                    () -> assertNotNull(newOrder.getPriceWithDiscount()),
                    () -> assertEquals(TOTAL_PRICE_WITH_20PCT_DISCOUNT, newOrder.getPriceWithDiscount()),
                    () -> assertNotNull(newOrder.getPaymentStatus()),
                    () -> assertEquals(PaymentStatus.DELIVERY, newOrder.getPaymentStatus()),
                    () -> assertNull(newOrder.getLinkToPaymentReceipt()),
                    () -> assertNotNull(newOrder.getNote()),
                    () -> assertEquals(TEST_NOTE, newOrder.getNote()),
                    () -> assertNotNull(newOrder.getUser()),
                    () -> assertNotNull(newOrder.getShippingAddress()),
                    () -> assertNotNull(newOrder.getCoupon()),
                    () -> assertNotNull(newOrder.getOrderItems())
            );


            // verify order items after (will pick up only order items with ORDER state)
            OrderFullResponse orderFullResponse = orderService.viewOneOrder(TEST_USER_ID, newOrder.getId());
            assertEquals(4, orderFullResponse.getOrderItems().size());

        }

        @WithMockCustomUser
        @DisplayName("Then new order is created without discount when coupon is empty")
        @Test
        public void testCheckoutCartAndProcessOrderWhenCouponIsEmpty() {
            // prepare
            final Long TEST_USER_ID = 12L;
            final BigDecimal TOTAL_PRICE = new BigDecimal("159.96");

            CheckoutCartRequest deliveryRequest = new CheckoutCartRequest();
            deliveryRequest.setNote(null);
            deliveryRequest.setCouponCode(null);
            deliveryRequest.setPaymentMethod(PAYMENT_METHOD_DELIVERY);
            deliveryRequest.setShippingAddressId(TEST_SHIPPING_ADDRESS_ID);

            // exercise
            OrderCompletedResponse orderCompletedResponse = orderService.checkoutCartAndProcessOrder(TEST_USER_ID, deliveryRequest);

            // verify
            assertAll("Verify orderCompletedResponse",
                    () -> assertNotNull(orderCompletedResponse),
                    () -> assertNotNull(orderCompletedResponse.getTotalPrice()),
                    () -> assertEquals(TOTAL_PRICE, orderCompletedResponse.getTotalPrice()),
                    () -> assertNotNull(orderCompletedResponse.getTotalPriceWithDiscount()),
                    () -> assertEquals(TOTAL_PRICE, orderCompletedResponse.getTotalPriceWithDiscount()),
                    () -> assertNull(orderCompletedResponse.getUsedCouponCode())
            );
        }

        @WithMockCustomUser
        @DisplayName("Then error is thrown when used coupon is passed")
        @Test
        public void testCheckoutCartFailsWhenRedeemedCouponIsPassed() {
            // prepare
            final String TEST_REDEEMED_COUPON_CODE = "1234aBaB56CD";

            CheckoutCartRequest deliveryRequest = new CheckoutCartRequest();
            deliveryRequest.setNote(TEST_NOTE);
            deliveryRequest.setCouponCode(TEST_REDEEMED_COUPON_CODE);
            deliveryRequest.setPaymentMethod(PAYMENT_METHOD_DELIVERY);
            deliveryRequest.setShippingAddressId(TEST_SHIPPING_ADDRESS_ID);

            // exercise
            assertThrows(CouponAlreadyRedeemedException.class,
                    () -> orderService.checkoutCartAndProcessOrder(TEST_USER_ID, deliveryRequest));
        }

        @WithMockCustomUser
        @DisplayName("Then error is thrown when shipping address is passed")
        @Test
        public void testCheckoutCartFailsWhenInvalidShippingAddressIsPassed() {
            // prepare
            final Long TEST_INVALID_SHIPPING_ADDRESS_ID = 9999L;

            CheckoutCartRequest deliveryRequest = new CheckoutCartRequest();
            deliveryRequest.setNote(TEST_NOTE);
            deliveryRequest.setCouponCode(TEST_COUPON_CODE);
            deliveryRequest.setPaymentMethod(PAYMENT_METHOD_DELIVERY);
            deliveryRequest.setShippingAddressId(TEST_INVALID_SHIPPING_ADDRESS_ID);

            // exercise
            assertThrows(EntityNotFoundException.class,
                    () -> orderService.checkoutCartAndProcessOrder(TEST_USER_ID, deliveryRequest));
        }

        @WithMockCustomUser
        @DisplayName("Then error is thrown when tried to checkout empty cart")
        @Test
        public void testCheckoutCartFailsWhenCartIsEmpty() {
            // prepare
            final Long TEST_USER_ID_EMPTY_CART = 11L;
            final Long TEST_SHIPPING_ADDRESS_ID = 11L;
            final String TEST_COUPON_CODE = "qwerty042020";

            CheckoutCartRequest deliveryRequest = new CheckoutCartRequest();
            deliveryRequest.setNote(TEST_NOTE);
            deliveryRequest.setCouponCode(TEST_COUPON_CODE);
            deliveryRequest.setPaymentMethod(PAYMENT_METHOD_DELIVERY);
            deliveryRequest.setShippingAddressId(TEST_SHIPPING_ADDRESS_ID);

            // exercise
            assertThrows(InvalidCartSizeException.class,
                    () -> orderService.checkoutCartAndProcessOrder(TEST_USER_ID_EMPTY_CART, deliveryRequest));
        }
    }

}
