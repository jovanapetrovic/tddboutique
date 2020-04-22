package com.jovana.service.impl.order;

import com.jovana.entity.order.dto.OrderCompletedResponse;
import com.jovana.entity.order.dto.CheckoutCartRequest;
import com.jovana.entity.order.dto.OrderResponse;

import java.util.Set;

/**
 * Created by jovana on 20.04.2020
 */
public interface OrderService {

    OrderCompletedResponse checkoutCartAndProcessOrder(Long userId, CheckoutCartRequest checkoutCartRequest);

    Set<OrderResponse> viewUserOrders(Long userId);

}
