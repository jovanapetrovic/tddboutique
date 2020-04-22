package com.jovana.service.impl;

import com.google.common.collect.Sets;
import com.jovana.entity.coupon.Coupon;
import com.jovana.entity.order.Order;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.PaymentStatus;
import com.jovana.entity.order.dto.*;
import com.jovana.entity.order.payment.PaymentResponse;
import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.order.OrderRepository;
import com.jovana.service.impl.coupon.CouponService;
import com.jovana.service.impl.order.OrderItemService;
import com.jovana.service.impl.order.OrderService;
import com.jovana.service.impl.order.OrderServiceImpl;
import com.jovana.service.impl.payment.PaymentService;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.util.RequestTestDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jovana on 21.04.2020
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;
    @Mock
    private ShippingAddressService shippingAddressService;
    @Mock
    private CouponService couponService;
    @Mock
    private OrderItemService orderItemService;
    @Mock
    private PaymentService paymentService;

    @DisplayName("When we want to checkout user cart and have order processed")
    @Nested
    class CheckoutCartAndProcessOrderTest {

        private final Long TEST_USER_ID = 10L;
        private final BigDecimal TEST_TOTAL_PRICE = new BigDecimal("49.98");
        private final BigDecimal TEST_TOTAL_PRICE_WITH_DISCOUNT = new BigDecimal("44.98");
        private final String TEST_PRODUCT_NAMES = "Casual dress, Evening dress";
        private final String TEST_COUPON_CODE = "ASDF5678asdf";
        private final String TEST_RECEIPT_URL = "https://pay.stripe.com/receipts/acct_12345";

        private CheckoutCartRequest checkoutCartWithCard;
        private CheckoutCartRequest checkoutCartWithDelivery;
        private CheckoutCartRequest checkoutCartWithDeliveryAndNoCoupon;
        private CheckoutCartRequest emptyCouponRequest;

        private User userMock;
        private ShippingAddress shippingAddressMock;
        private Coupon couponMock;
        private OrderSummary orderSummaryMock;
        private PaymentResponse paymentResponseMock;

        private OrderItem orderItemMock1;
        private OrderItem orderItemMock2;
        private List<OrderItem> orderItemsListMock = new ArrayList<>();

        private Order orderMock;

        @BeforeEach
        public void setUp() {
            checkoutCartWithCard = RequestTestDataProvider.getCheckoutCartRequests().get("card");
            checkoutCartWithDelivery = RequestTestDataProvider.getCheckoutCartRequests().get("delivery");
            checkoutCartWithDeliveryAndNoCoupon = RequestTestDataProvider.getCheckoutCartRequests().get("deliveryNoCoupon");
            emptyCouponRequest = RequestTestDataProvider.getCheckoutCartRequests().get("emptyCouponDeliveryRequest");

            userMock = mock(User.class);
            shippingAddressMock = mock(ShippingAddress.class);
            couponMock = mock(Coupon.class);
            orderSummaryMock = mock(OrderSummary.class);
            paymentResponseMock = mock(PaymentResponse.class);

            orderItemMock1 = mock(OrderItem.class);
            orderItemMock2 = mock(OrderItem.class);
            orderItemsListMock.add(orderItemMock1);
            orderItemsListMock.add(orderItemMock2);

            orderMock = mock(Order.class);
        }

        @DisplayName("Then payment is charged and new order is created when valid " +
                "CheckoutCartRequest(couponCode, CARD, shippingAddressId) is provided")
        @Test
        public void testCheckoutCartWithCouponAndCardPaymentMethodSuccess() {
            // prepare
            when(userService.getUserById(anyLong())).thenReturn(userMock);

            when(shippingAddressService.getUserShippingAddressById(anyLong())).thenReturn(shippingAddressMock);

            when(couponService.checkIfCouponIsValid(anyLong(), anyString())).thenReturn(couponMock);

            when(orderItemService.getOrderSummary(anyLong())).thenReturn(orderSummaryMock);
            when(orderSummaryMock.getTotalPrice()).thenReturn(TEST_TOTAL_PRICE);
            when(orderSummaryMock.getProductNames()).thenReturn(TEST_PRODUCT_NAMES);

            when(couponService.calculatePriceWithDiscount(couponMock, TEST_TOTAL_PRICE)).thenReturn(TEST_TOTAL_PRICE_WITH_DISCOUNT);

            when(paymentService.payOrder(TEST_TOTAL_PRICE_WITH_DISCOUNT, TEST_PRODUCT_NAMES)).thenReturn(paymentResponseMock);
            when(paymentResponseMock.getReceiptUrl()).thenReturn(TEST_RECEIPT_URL);

            when(couponService.redeemCoupon(any(Coupon.class))).thenReturn(true);
            when(couponMock.getCode()).thenReturn(TEST_COUPON_CODE);

            when(orderRepository.save(any(Order.class))).thenReturn(orderMock);

            when(orderItemService.updateCartItemsToOrderItems(Sets.newHashSet(), orderMock)).thenReturn(orderItemsListMock);

            // exercise
            OrderCompletedResponse orderCompletedResponse = orderService.checkoutCartAndProcessOrder(TEST_USER_ID, checkoutCartWithCard);

            // verify
            assertAll("Verify order completed response (card)",
                    () -> assertNotNull(orderCompletedResponse),
                    () -> assertNotNull(orderCompletedResponse.getTotalPrice()),
                    () -> assertEquals(TEST_TOTAL_PRICE, orderCompletedResponse.getTotalPrice()),
                    () -> assertNotNull(orderCompletedResponse.getTotalPriceWithDiscount()),
                    () -> assertEquals(TEST_TOTAL_PRICE_WITH_DISCOUNT, orderCompletedResponse.getTotalPriceWithDiscount()),
                    () -> assertNotNull(orderCompletedResponse.getUsedCouponCode()),
                    () -> assertEquals(TEST_COUPON_CODE, orderCompletedResponse.getUsedCouponCode()),
                    () -> assertNotNull(orderCompletedResponse.getPaymentStatusDescription()),
                    () -> assertEquals(PaymentStatus.PAID.getDescription(), orderCompletedResponse.getPaymentStatusDescription()),
                    () -> assertNotNull(orderCompletedResponse.getReceiptUrl()),
                    () -> assertEquals(TEST_RECEIPT_URL, orderCompletedResponse.getReceiptUrl())
            );
        }

        @DisplayName("Then new order is created when valid " +
                "CheckoutCartRequest(couponCode, DELIVERY, shippingAddressId) is provided")
        @Test
        public void testCheckoutCartWithCouponAndDeliveryPaymentMethodSuccess() {
            // prepare
            when(userService.getUserById(anyLong())).thenReturn(userMock);

            when(shippingAddressService.getUserShippingAddressById(anyLong())).thenReturn(shippingAddressMock);

            when(couponService.checkIfCouponIsValid(anyLong(), anyString())).thenReturn(couponMock);

            when(orderItemService.getOrderSummary(anyLong())).thenReturn(orderSummaryMock);
            when(orderSummaryMock.getTotalPrice()).thenReturn(TEST_TOTAL_PRICE);

            when(couponService.calculatePriceWithDiscount(couponMock, TEST_TOTAL_PRICE)).thenReturn(TEST_TOTAL_PRICE_WITH_DISCOUNT);

            when(couponService.redeemCoupon(any(Coupon.class))).thenReturn(true);
            when(couponMock.getCode()).thenReturn(TEST_COUPON_CODE);

            when(orderRepository.save(any(Order.class))).thenReturn(orderMock);

            when(orderItemService.updateCartItemsToOrderItems(Sets.newHashSet(), orderMock)).thenReturn(orderItemsListMock);

            // exercise
            OrderCompletedResponse orderCompletedResponse = orderService.checkoutCartAndProcessOrder(TEST_USER_ID, checkoutCartWithDelivery);

            // verify
            assertAll("Verify order completed response (delivery)",
                    () -> assertNotNull(orderCompletedResponse),
                    () -> assertNotNull(orderCompletedResponse.getTotalPrice()),
                    () -> assertEquals(TEST_TOTAL_PRICE, orderCompletedResponse.getTotalPrice()),
                    () -> assertNotNull(orderCompletedResponse.getTotalPriceWithDiscount()),
                    () -> assertEquals(TEST_TOTAL_PRICE_WITH_DISCOUNT, orderCompletedResponse.getTotalPriceWithDiscount()),
                    () -> assertNotNull(orderCompletedResponse.getUsedCouponCode()),
                    () -> assertEquals(TEST_COUPON_CODE, orderCompletedResponse.getUsedCouponCode()),
                    () -> assertNotNull(orderCompletedResponse.getPaymentStatusDescription()),
                    () -> assertEquals(PaymentStatus.DELIVERY.getDescription(), orderCompletedResponse.getPaymentStatusDescription()),
                    () -> assertNull(orderCompletedResponse.getReceiptUrl())
            );
        }

        @DisplayName("Then new order is created when valid CheckoutCartRequest(DELIVERY, shippingAddressId) is provided")
        @Test
        public void testCheckoutCartWithoutCouponAndDeliveryPaymentMethodSuccess() {
            // prepare
            when(userService.getUserById(anyLong())).thenReturn(userMock);

            when(shippingAddressService.getUserShippingAddressById(anyLong())).thenReturn(shippingAddressMock);

            when(orderItemService.getOrderSummary(anyLong())).thenReturn(orderSummaryMock);
            when(orderSummaryMock.getTotalPrice()).thenReturn(TEST_TOTAL_PRICE);

            when(orderRepository.save(any(Order.class))).thenReturn(orderMock);

            when(orderItemService.updateCartItemsToOrderItems(Sets.newHashSet(), orderMock)).thenReturn(orderItemsListMock);

            // exercise
            OrderCompletedResponse orderCompletedResponse = orderService.checkoutCartAndProcessOrder(TEST_USER_ID, checkoutCartWithDeliveryAndNoCoupon);

            // verify
            assertAll("Verify order completed response without coupon",
                    () -> assertNotNull(orderCompletedResponse),
                    () -> assertNull(orderCompletedResponse.getUsedCouponCode())
            );
        }

        @DisplayName("Then new order is created when valid CheckoutCartRequest(empty_coupon, DELIVERY, shippingAddressId) is provided")
        @Test
        public void testCheckoutCartWithEmptyCouponSideCase() {
            // prepare
            when(userService.getUserById(anyLong())).thenReturn(userMock);

            when(shippingAddressService.getUserShippingAddressById(anyLong())).thenReturn(shippingAddressMock);

            when(orderItemService.getOrderSummary(anyLong())).thenReturn(orderSummaryMock);
            when(orderSummaryMock.getTotalPrice()).thenReturn(TEST_TOTAL_PRICE);

            when(orderRepository.save(any(Order.class))).thenReturn(orderMock);

            when(orderItemService.updateCartItemsToOrderItems(Sets.newHashSet(), orderMock)).thenReturn(orderItemsListMock);

            // exercise
            OrderCompletedResponse orderCompletedResponse = orderService.checkoutCartAndProcessOrder(TEST_USER_ID, emptyCouponRequest);

            // verify
            assertAll("Verify order completed response without coupon",
                    () -> assertNotNull(orderCompletedResponse),
                    () -> assertNull(orderCompletedResponse.getUsedCouponCode())
            );
        }

    }

    @DisplayName("When we want to view one or more user orders")
    @Nested
    class GetUserOrdersTest {

        private final Long TEST_USER_ID = 10L;
        private final Long TEST_ORDER_ID = 10L;
        private final Long TEST_ORDER_ID_NOT_EXISTS = 9999L;

        @DisplayName("Then all user's orders are fetched from database if there are any")
        @Test
        public void testViewUserOrdersSuccess() {
            // prepare
            Order orderMock1 = mock(Order.class);
            Order orderMock2 = mock(Order.class);
            Set<Order> orders = org.mockito.internal.util.collections.Sets.newSet(orderMock1, orderMock2);

            when(orderRepository.findAllByUserId(TEST_USER_ID)).thenReturn(orders);

            when(orderMock1.getPaymentStatus()).thenReturn(PaymentStatus.PAID);
            when(orderMock2.getPaymentStatus()).thenReturn(PaymentStatus.DELIVERY);

            // exercise
            Set<OrderResponse> orderResponses = orderService.viewUserOrders(TEST_USER_ID);

            // verify
            assertNotNull(orderResponses);
            assertEquals(2, orderResponses.size());
        }

        @DisplayName("Then Order with order items is fetched from database when id is valid")
        @Test
        public void testViewOneOrderSuccess() {
            // prepare
            OrderFullResponse orderResponseMock = mock(OrderFullResponse.class);
            when(orderRepository.findOneWithOrderItemsByOrderIdAndUserId(anyLong(), anyLong())).thenReturn(orderResponseMock);
            // exercise
            OrderFullResponse orderFullResponse = orderService.viewOneOrder(TEST_USER_ID, TEST_ORDER_ID);
            // verify
            assertNotNull(orderFullResponse);
        }

        @DisplayName("Then error is thrown when Order with passed id doesn't exist")
        @Test
        public void testViewOneOrderFailsWhenPassedIdDoesntExist() {
            // prepare
            when(orderRepository.findOneWithOrderItemsByOrderIdAndUserId(anyLong(), anyLong())).thenReturn(null);
            // verify
            assertThrows(EntityNotFoundException.class, () -> orderService.viewOneOrder(TEST_USER_ID, TEST_ORDER_ID_NOT_EXISTS));
        }
    }

}
