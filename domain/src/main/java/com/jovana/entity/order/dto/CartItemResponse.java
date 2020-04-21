package com.jovana.entity.order.dto;

import com.google.common.collect.Lists;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.image.Image;
import com.jovana.entity.product.image.dto.ImageResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jovana on 20.04.2020
 */
public class CartItemResponse {

    private Long orderItemId;
    private Long productId;
    private String productName;
    private String productSize;
    private String productColor;
    private List<ImageResponse> images;
    private Long quantity;
    private BigDecimal productPrice;
    private BigDecimal totalPricePerProduct;

    public CartItemResponse() {
    }

    // used by Hibernate
    public CartItemResponse(OrderItem orderItem, Product product) {
        this.setOrderItemId(orderItem.getId());
        this.setProductId(product.getId());
        this.setProductName(product.getName());
        this.setProductSize(orderItem.getProductSize().getCode());
        this.setProductColor(orderItem.getProductColor().getCode());

        List<ImageResponse> imageResponses = Lists.newArrayList();
        for (Image i : product.getImages()) {
            imageResponses.add(ImageResponse.createFromImage(i));
        }
        this.setImages(imageResponses);

        this.setQuantity(orderItem.getQuantity());
        this.setProductPrice(product.getPrice());
        this.setTotalPricePerProduct(orderItem.getTotalPricePerProduct());
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getTotalPricePerProduct() {
        return totalPricePerProduct;
    }

    public void setTotalPricePerProduct(BigDecimal totalPricePerProduct) {
        this.totalPricePerProduct = totalPricePerProduct;
    }

}
