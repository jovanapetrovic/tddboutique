package com.jovana.entity.order.dto;

import java.util.Set;

/**
 * Created by jovana on 18.04.2020
 */
public class CartResponse {

    private Set<CartItemDTO> orderedProducts;
    private Set<CartItemDTO> outOfStockProducts;

    public CartResponse() {
    }

    public CartResponse(Set<CartItemDTO> orderedProducts, Set<CartItemDTO> outOfStockProducts) {
        this.orderedProducts = orderedProducts;
        this.outOfStockProducts = outOfStockProducts;
    }

    public Set<CartItemDTO> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(Set<CartItemDTO> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public Set<CartItemDTO> getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public void setOutOfStockProducts(Set<CartItemDTO> outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }

}
