package com.jovana.entity.product.dto;

import com.google.common.collect.Lists;
import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.SizeCode;
import com.jovana.entity.product.image.Image;
import com.jovana.entity.product.image.dto.ImageResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jovana on 13.04.2020
 */
public class ProductFullResponse {

    private Long id;
    private String name;
    private String material;
    private String description;
    private BigDecimal price;
    private List<String> sizes;
    private List<String> colors;
    private boolean inStock;
    private List<ImageResponse> images;

    public ProductFullResponse() {
    }

    // used by Hibernate
    public ProductFullResponse(Product product) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setMaterial(product.getMaterial());
        this.setDescription(product.getDescription());
        this.setPrice(product.getPrice());
        this.setSizes(SizeCode.getStringsFromSizeCodes(product.getSizes()));
        this.setColors(ColorCode.getStringsFromColorCodes(product.getColors()));
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
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
