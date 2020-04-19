package com.jovana.entity.product.dto;

import com.google.common.collect.Lists;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.image.Image;
import com.jovana.entity.product.image.dto.ImageResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jovana on 13.04.2020
 */
public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private boolean inStock;
    private List<ImageResponse> images;

    public ProductResponse() {
    }

    // used by Hibernate
    public ProductResponse(Product product) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setPrice(product.getPrice());
        this.setInStock(product.getStock().getNumberOfUnitsInStock() > 0);

        List<ImageResponse> imageResponses = Lists.newArrayList();
        for (Image i : product.getImages()) {
            imageResponses.add(ImageResponse.createFromImage(i));
        }
        this.setImages(imageResponses);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
