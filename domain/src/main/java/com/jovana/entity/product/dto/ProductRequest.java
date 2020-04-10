package com.jovana.entity.product.dto;

import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.SizeCode;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by jovana on 10.04.2020
 */
public class ProductRequest {

    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    @NotNull
    @Size(min = 3, max = 100)
    private String material;

    @Size(max = 100)
    private String description;

    @NotNull
    @DecimalMin("1.0")
    @DecimalMax("500.0")
    private BigDecimal price;

    @Size(min = 1)
    private Set<SizeCode> sizes;

    @Size(min = 1)
    private Set<ColorCode> colors;

    private Long numberOfUnitsInStock;

    public ProductRequest() {
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

    public Set<SizeCode> getSizes() {
        return sizes;
    }

    public void setSizes(Set<SizeCode> sizes) {
        this.sizes = sizes;
    }

    public Set<ColorCode> getColors() {
        return colors;
    }

    public void setColors(Set<ColorCode> colors) {
        this.colors = colors;
    }

    public Long getNumberOfUnitsInStock() {
        return numberOfUnitsInStock;
    }

    public void setNumberOfUnitsInStock(Long numberOfUnitsInStock) {
        this.numberOfUnitsInStock = numberOfUnitsInStock;
    }

}
