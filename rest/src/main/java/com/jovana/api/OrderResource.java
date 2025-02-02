package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.order.dto.*;
import com.jovana.entity.product.dto.ProductFullResponse;
import com.jovana.service.impl.order.OrderItemService;
import com.jovana.service.impl.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * Created by jovana on 13.04.2020
 */
@RestController
@RequestMapping(value = PathConstants.API)
public class OrderResource {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    @PostMapping(value = PathConstants.CART_ADD)
    public ResponseEntity<AddToCartResponse> addItemsToCartPOST(@PathVariable("userId") Long userId,
                                                                @Valid @RequestBody CartRequest cartRequest) {
        AddToCartResponse addToCartResponse = orderItemService.addItemsToCart(userId, cartRequest);
        return new ResponseEntity<>(addToCartResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = PathConstants.CART_VIEW_ALL)
    public ResponseEntity<CartResponse> viewCartGET(@PathVariable("userId") Long userId) {
        CartResponse cartResponse = orderItemService.viewCart(userId);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = PathConstants.CART_REMOVE_ITEM)
    public ResponseEntity<Void> removeItemsFromCartDELETE(@PathVariable("userId") Long userId,
                                                          @PathVariable("orderItemId") Long orderItemId) {
        orderItemService.removeItemFromCart(userId, orderItemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = PathConstants.CART_CHECKOUT)
    public ResponseEntity<OrderCompletedResponse> checkoutCartAndProcessOrderPOST(@PathVariable("userId") Long userId,
                                                                                  @Valid @RequestBody CheckoutCartRequest checkoutCartRequest) {
        OrderCompletedResponse orderCompletedResponse = orderService.checkoutCartAndProcessOrder(userId, checkoutCartRequest);
        return new ResponseEntity<>(orderCompletedResponse, HttpStatus.OK);
    }

    @GetMapping(value = PathConstants.ORDER_VIEW_ALL)
    public ResponseEntity<Set<OrderResponse>> viewAllUserOrdersGET(@PathVariable("userId") Long userId) {
        Set<OrderResponse> orderResponses = orderService.viewUserOrders(userId);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    @GetMapping(value = PathConstants.ORDER_VIEW_ONE)
    public ResponseEntity<OrderFullResponse> viewOneUserOrderGET(@PathVariable("userId") Long userId,
                                                                 @PathVariable("orderId") Long orderId) {
        OrderFullResponse orderFullResponse = orderService.viewOneOrder(userId, orderId);
        return new ResponseEntity<>(orderFullResponse, HttpStatus.OK);
    }

}
