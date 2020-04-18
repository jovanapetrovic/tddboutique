package com.jovana.entity.order.dto;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by jovana on 13.04.2020
 */
public class CartRequest {

    @Size(min = 1, max = 10)
    private List<CartItemDTO> cartItems;

    public CartRequest() {
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }

}
