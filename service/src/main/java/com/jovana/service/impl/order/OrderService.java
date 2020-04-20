package com.jovana.service.impl.order;

import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.dto.CartRequest;
import com.jovana.entity.order.dto.AddToCartResponse;
import com.jovana.entity.order.dto.CartResponse;

/**
 * Created by jovana on 13.04.2020
 */
public interface OrderService {

    OrderItem getOrderItemById(Long cartItemId);

    AddToCartResponse addItemsToCart(Long userId, CartRequest cartRequest);

    boolean removeItemFromCart(Long userId, Long orderItemId);

    CartResponse viewCart(Long userId);

}
