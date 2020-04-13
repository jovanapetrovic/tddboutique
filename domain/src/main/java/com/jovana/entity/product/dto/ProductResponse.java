package com.jovana.entity.product.dto;

import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.SizeCode;
import com.jovana.entity.product.image.dto.ImageResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jovana on 13.04.2020
 */
public class ProductResponse {

    private String name;

    private BigDecimal price;

    private boolean inStock;

    private List<ImageResponse> images;

    public ProductResponse() {
    }

    public static ProductResponse createFromProduct(Product product, List<ImageResponse> imageResponses) {
        ProductResponse response = new ProductResponse();
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setInStock(product.getStock().getNumberOfUnitsInStock() > 0);
        response.setImages(imageResponses);
        return response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }

}
