package com.jovana.service.impl.order;

import com.google.common.collect.Sets;
import com.jovana.entity.coupon.Coupon;
import com.jovana.entity.order.Order;
import com.jovana.entity.order.PaymentMethod;
import com.jovana.entity.order.dto.*;
import com.jovana.entity.order.PaymentStatus;
import com.jovana.entity.order.payment.PaymentResponse;
import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.order.OrderRepository;
import com.jovana.service.impl.coupon.CouponService;
import com.jovana.service.impl.payment.PaymentService;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.security.IsAdminOrUser;
import com.jovana.service.security.IsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;


/**
 * Created by jovana on 20.04.2020
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private PaymentService paymentService;

    @IsUser
    @Override
    public OrderCompletedResponse checkoutCartAndProcessOrder(Long userId, CheckoutCartRequest checkoutCartRequest) {
        boolean isCouponCodeAdded = checkoutCartRequest.getCouponCode() != null && !checkoutCartRequest.getCouponCode().isEmpty();

        // Step 1: Validate & fetch needed data
        User user = userService.getUserById(userId);
        ShippingAddress shippingAddress = shippingAddressService.getUserShippingAddressById(checkoutCartRequest.getShippingAddressId());

        Coupon coupon = null;
        if (isCouponCodeAdded) {
            coupon = couponService.checkIfCouponIsValid(userId, checkoutCartRequest.getCouponCode());
        }
        OrderSummary orderSummary = orderItemService.getOrderSummary(userId);

        // Step 2: Calculate price with discount if there is a coupon
        BigDecimal totalPriceWithDiscount = orderSummary.getTotalPrice();
        if (isCouponCodeAdded) {
            totalPriceWithDiscount = couponService.calculatePriceWithDiscount(coupon, orderSummary.getTotalPrice());
        }

        // Step 3: Try to charge payment if payment method is card
        PaymentResponse paymentResponse = null;
        boolean isCardPayment = PaymentMethod.CARD.equals(PaymentMethod.valueOf(checkoutCartRequest.getPaymentMethod()));
        if (isCardPayment) {
            paymentResponse = paymentService.payOrder(totalPriceWithDiscount, orderSummary.getProductNames());
        }

        // Step 4: Mark coupon as redeemed
        if (isCouponCodeAdded) {
            couponService.redeemCoupon(coupon);
        }

        // Step 5: Create new Order entry
        Order newOrder = createNewOrder(
                user,
                shippingAddress,
                checkoutCartRequest.getNote(),
                coupon,
                orderSummary,
                isCardPayment,
                paymentResponse != null ? paymentResponse.getReceiptUrl() : null,
                totalPriceWithDiscount);

        // Step 6: Update status for all order items and assign order to them
        orderItemService.updateCartItemsToOrderItems(orderSummary.getOrderItems(), newOrder);

        // Step 7: Prepare response for user
        return new OrderCompletedResponse(
                orderSummary.getTotalPrice(),
                totalPriceWithDiscount,
                coupon != null ? coupon.getCode() : "",
                isCardPayment,
                paymentResponse != null ? paymentResponse.getReceiptUrl() : null
        );
    }

    @IsAdminOrUser
    @Override
    public Set<OrderResponse> viewUserOrders(Long userId) {
        Set<Order> orders = orderRepository.findAllByUserId(userId);
        Set<OrderResponse> orderResponses = Sets.newHashSet();
        for (Order order : orders) {
            orderResponses.add(OrderResponse.createFromOrder(order));
        }
        LOGGER.debug("Found {} orders for user with id = {}.", orderResponses.size(), userId);
        return orderResponses;
    }

    @IsAdminOrUser
    @Override
    public OrderFullResponse viewOneOrder(Long userId, Long orderId) {
        OrderFullResponse orderFullResponse = orderRepository.findOneWithOrderItemsByOrderIdAndUserId(userId, orderId);
        if (orderFullResponse == null) {
            LOGGER.info("Order with id = {} was not found in the db.", orderId);
            throw new EntityNotFoundException("No order found with id = " + orderId);
        }
        return orderFullResponse;
    }

    private Order createNewOrder(User user,
                                 ShippingAddress shippingAddress,
                                 String note,
                                 Coupon coupon,
                                 OrderSummary orderSummary,
                                 boolean isCardPayment,
                                 String receiptUrl,
                                 BigDecimal priceWithDiscount) {
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setNote(note);
        if (isCardPayment) {
            order.setPaymentStatus(PaymentStatus.PAID);
        } else {
            order.setPaymentStatus(PaymentStatus.DELIVERY);
        }
        if (coupon != null) {
            order.setCoupon(coupon);
        }
        order.setTotalPrice(orderSummary.getTotalPrice());
        order.setPriceWithDiscount(priceWithDiscount);
        order.setLinkToPaymentReceipt(receiptUrl);

        return orderRepository.save(order);
    }

}
