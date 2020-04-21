package com.jovana.service.impl.order;

import com.jovana.entity.order.Order;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.dto.CartRequest;
import com.jovana.entity.order.dto.AddToCartResponse;
import com.jovana.entity.order.dto.CartResponse;
import com.jovana.entity.order.dto.OrderSummary;

import java.util.List;
import java.util.Set;

/**
 * Created by jovana on 13.04.2020
 */
public interface OrderItemService {

    OrderItem getOrderItemById(Long cartItemId);

    AddToCartResponse addItemsToCart(Long userId, CartRequest cartRequest);

    boolean removeItemFromCart(Long userId, Long orderItemId);

    CartResponse viewCart(Long userId);

    OrderSummary getOrderSummary(Long userId);

    List<OrderItem> updateCartItemsToOrderItems(Set<OrderItem> orderItems, Order order);

}
