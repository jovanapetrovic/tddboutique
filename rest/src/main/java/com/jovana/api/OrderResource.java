package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.order.dto.CartRequest;
import com.jovana.entity.order.dto.CartResponse;
import com.jovana.service.impl.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Created by jovana on 13.04.2020
 */
@RestController
@RequestMapping(value = PathConstants.API)
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = PathConstants.CART_ADD)
    public ResponseEntity<CartResponse> addItemsToCartPOST(@PathVariable("userId") Long userId,
                                                           @Valid @RequestBody CartRequest cartRequest) {
        CartResponse cartResponse = orderService.addItemsToCart(userId, cartRequest);
        return new ResponseEntity<>(cartResponse, HttpStatus.CREATED);
    }

}
