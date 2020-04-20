package com.jovana.entity.order.dto;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by jovana on 20.04.2020
 */
public class CartResponse {

    private Set<CartItemResponse> cartItems;
    private BigDecimal totalPrice;

    public CartResponse() {
    }

    public CartResponse(Set<CartItemResponse> cartItems, BigDecimal totalPrice) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }

    public Set<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItemResponse> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}
