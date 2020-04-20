package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.order.dto.CartRequest;
import com.jovana.entity.order.dto.AddToCartResponse;
import com.jovana.entity.order.dto.CartResponse;
import com.jovana.service.impl.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jovana on 13.04.2020
 */
@RestController
@RequestMapping(value = PathConstants.API)
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = PathConstants.CART_ADD)
    public ResponseEntity<AddToCartResponse> addItemsToCartPOST(@PathVariable("userId") Long userId,
                                                                @Valid @RequestBody CartRequest cartRequest) {
        AddToCartResponse addToCartResponse = orderService.addItemsToCart(userId, cartRequest);
        return new ResponseEntity<>(addToCartResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = PathConstants.CART_VIEW_ALL)
    public ResponseEntity<CartResponse> viewCartGET(@PathVariable("userId") Long userId) {
        CartResponse cartResponse = orderService.viewCart(userId);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = PathConstants.CART_REMOVE_ITEM)
    public ResponseEntity<Void> removeItemsFromCartDELETE(@PathVariable("userId") Long userId,
                                                          @PathVariable("orderItemId") Long orderItemId) {
        orderService.removeItemFromCart(userId, orderItemId);
        return ResponseEntity.ok().build();
    }

}
