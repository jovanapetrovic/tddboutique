package com.jovana.entity.order.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Created by jovana on 13.04.2020
 */
public class CartItemDTO {

    @NotNull
    private Long productId;

    @NotNull
    private String productSize;

    @NotNull
    private String productColor;

    @Positive
    @NotNull
    private Long quantity;

    public CartItemDTO() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
